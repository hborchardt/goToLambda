package gotolambda.actions;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.debug.ui.IJavaDebugUIConstants;
import org.eclipse.jdt.internal.debug.core.model.JDIInterfaceType;
import org.eclipse.jdt.internal.debug.core.model.JDIObjectValue;
import org.eclipse.jdt.internal.debug.core.model.JDIVariable;
import org.eclipse.jdt.internal.debug.ui.actions.OpenVariableConcreteTypeAction;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;

import gotolambda.util.ConstantPool;
import gotolambda.util.JvmOpcode;

public class GoToLambdaAction extends OpenVariableConcreteTypeAction {

	@Override
	public boolean openElement(IAction action, Object element) throws DebugException, CoreException {
		var line = 0;
		if (element instanceof JDIVariable) {
			final var jdiVariable = (JDIVariable) element;
			if (isInterfaceType(jdiVariable)) {
				final var val = (JDIObjectValue) jdiVariable.getValue();
				if (val.getJavaType().toString().contains("$$Lambda")) { //$NON-NLS-1$
					try {
						Location l = getLambdaLocation(val);
						if (l != null) {
							line = l.lineNumber();
						}
					} catch (DebugException e) {
						return false;
					}
				}
			}
		}

		IType sourceElement = resolveSourceElement(element);
		if (sourceElement != null) {
			var part = JavaUI.openInEditor(sourceElement);

			ITextEditor editor = (ITextEditor) part;
			IDocumentProvider provider = editor.getDocumentProvider();
			IDocument document = provider.getDocument(editor.getEditorInput());
			try {
				int start = document.getLineOffset(line - 1);
				editor.selectAndReveal(start, 0);
			} catch (BadLocationException e) {
				return false;
			}
			return false;
		}
		IStatus status = new Status(IStatus.INFO, IJavaDebugUIConstants.PLUGIN_ID, IJavaDebugUIConstants.INTERNAL_ERROR,
				"Source not found", null);
		throw new CoreException(status);
	}

	private boolean isInterfaceType(JDIVariable jdiVariable) {
		try {
			return jdiVariable.getJavaType() instanceof JDIInterfaceType;
		} catch (DebugException e) {
			return false;
		}
	}

	public Location getLambdaLocation(JDIObjectValue val) throws CoreException {
		try {
			ReferenceType underlyingType = val.getUnderlyingObject().referenceType();
			var cp = underlyingType.constantPool();
			var cpEntries = new ConstantPool(cp);
			// pick the "run" method. There should be two methods in the functional
			// interface, and one is the constructor. Pick the other one.
			var method = underlyingType.methods().get(0);
			if (method.name().equals("<init>")) {
				method = underlyingType.methods().get(1);
			}
			var bytecode = method.bytecodes();
			// search the invokestatic bytecode using the known patterns
			var invokestaticBytecodeIdx = -1;
			for (int i = 0; i < bytecode.length;) {
				var opcode = JvmOpcode.find(bytecode[i]);
				if (opcode == JvmOpcode.INVOKESTATIC || opcode == JvmOpcode.INVOKEDYNAMIC || opcode == JvmOpcode.INVOKEVIRTUAL) {
					invokestaticBytecodeIdx = i;
					break;
				}
				if (opcode == null) {
					break;
				}
				i += opcode.getLen();
			}
			if (invokestaticBytecodeIdx == -1) {
				IStatus status = new Status(IStatus.ERROR, IJavaDebugUIConstants.PLUGIN_ID,
						IJavaDebugUIConstants.INTERNAL_ERROR, "No function call found in bytecode sequence for lambda",
						new RuntimeException("Bytecode is: " + Arrays.toString(bytecode)));
				throw new CoreException(status);
			}
			// argument to invokestatic is the index of a MethodRef in the constant pool.
			// Chase the MethodRef until we get the name of the static function.
			var methodRefIdx = readUint16(bytecode, invokestaticBytecodeIdx + 1);
			var methodRef = cpEntries.get(methodRefIdx);
			var clsIdx = readUint16(cp, methodRef.startIdx);
			var cls = cpEntries.get(clsIdx);
			var clsNameIdx = readUint16(cp, cls.startIdx);
			var clsNameEntry = cpEntries.get(clsNameIdx);
			var clsName = new String(cp, clsNameEntry.startIdx + 2, clsNameEntry.len - 2);
			var methodNameAndTypeIdx = readUint16(cp, methodRef.startIdx + 2);
			var methodNameAndType = cpEntries.get(methodNameAndTypeIdx);
			var methodNameIdx = readUint16(cp, methodNameAndType.startIdx);
			var methodNameEntry = cpEntries.get(methodNameIdx);
			var methodName = new String(cp, methodNameEntry.startIdx + 2, methodNameEntry.len - 2);
			// we know the defining class of the lambda from the name of the lambda type
			//var containingClass = underlyingType.name().split("\\$\\$")[0];

			return underlyingType.virtualMachine().classesByName(clsName.replace('/','.')).get(0).methodsByName(methodName).get(0)
					.location();
		} catch (RuntimeException e) {
			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, IJavaDebugUIConstants.PLUGIN_ID,
					IJavaDebugUIConstants.INTERNAL_ERROR, "Exception", e);
			throw new CoreException(status);
		}
	}

	private int readUint16(byte[] arr, int idx) {
		return Byte.toUnsignedInt(arr[idx]) * 256 + Byte.toUnsignedInt(arr[idx + 1]);
	}

}
