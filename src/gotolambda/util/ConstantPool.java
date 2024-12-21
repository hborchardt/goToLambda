package gotolambda.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of constant pool entries, created by parsing given constant pool
 * bytes.
 */
public class ConstantPool {
	public static class Entry {
		public byte type;
		public int len;
		public int startIdx;

		public Entry(byte type, int len, int startIdx) {
			this.type = type;
			this.len = len;
			this.startIdx = startIdx;
		}

		@Override
		public String toString() {
			return "ConstantPool.Entry " + type + " " + len + " " + startIdx;
		}
	}

	public List<Entry> entries = new ArrayList<>();

	public ConstantPool(byte[] cp) {
		// constant pool starts at index 1 - so add a dummy entry at 0
		var fakeEntry = new Entry((byte) 0, 0, 0);
		entries.add(fakeEntry);
		for (int i = 0; i < cp.length;) {
			int len = 0;
			switch (cp[i]) {
			case 7: // Class(7, 2),
			case 8: // String(8, 2),
			case 16: // MethodType(16, 2),
			case 19: // ModuleInfo(19, 2),
			case 20: // PackageInfo(20, 2);
				len = 2;
				break;
			case 15: // MethodHandle(15, 3),
				len = 3;
				break;
			case 9: // Fieldref(9, 4),
			case 10: // Methodref(10, 4),
			case 11: // InterfaceMethodRef(11, 4),
			case 3: // Integer(3, 4),
			case 4: // Float(4, 4),
			case 12: // NameAndType(12, 4),
			case 17: // Dynamic(17, 4),
			case 18: // InvokeDynamic(18, 4),
				len = 4;
				break;
			case 5: // Long(5, 8),
			case 6: // Double(6, 8),
				len = 8;
				break;
			case 1: // Utf8(1, -1),
				// Utf8 is the only dynamically sized one. Handle specially.
				len = Byte.toUnsignedInt(cp[i + 1]) * 256 + Byte.toUnsignedInt(cp[i + 2]) + 2;
				break;
			}
			entries.add(new Entry(cp[i], len, i + 1));
			if (cp[i] == 5 || cp[i] == 6) {
				// length 8 entries require two slots in the constant pool
				entries.add(fakeEntry);
			}
			i += len + 1;
		}
	}

	public Entry get(int idx) {
		return entries.get(idx);
	}
}