package gotolambda;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.ui.display.JavaInspectExpression;

/**
 * Converts an IJavaVariable to a LambdaJavaVariable, when the variable value is
 * a Lambda function.
 * <p>
 * This means that checking whether a variable can be adapted to a
 * LambdaJavaVariable can be used to test whether a variable is a lambda.
 */
public class LambdaJavaVariableAdapterFactory implements IAdapterFactory {

	/**
	 * @see IAdapterFactory#getAdapter(Object, Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Object obj, Class<T> adapterType) {
		if (adapterType.isInstance(obj)) {
			return (T) obj;
		}
		var isLambda = false;
		try {
			if (adapterType == LambdaJavaVariable.class) {
				if (obj instanceof IJavaVariable jvar) {
					IValue value = jvar.getValue();
					if (value != null) {
						isLambda = value.getReferenceTypeName().contains("$$Lambda");
					}
				}
				if (obj instanceof JavaInspectExpression jexp) {
					IValue value = jexp.getValue();
					if (value != null) {
						isLambda = value.getReferenceTypeName().contains("$$Lambda");
					}
				}
			}
		} catch (DebugException e) {
			return null;
		}
		if (isLambda) {
			return (T) new LambdaJavaVariable();
		}
		return null;
	}

	/**
	 * @see IAdapterFactory#getAdapterList()
	 */
	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { LambdaJavaVariable.class };
	}
}
