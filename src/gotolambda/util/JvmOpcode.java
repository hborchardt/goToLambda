package gotolambda.util;

/**
 * List of jvm opcodes together with the number of bytes for this instruction.
 * <p>
 * Extracted from
 * https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html via
 *
 * <pre>
 * var sections = document.querySelectorAll(".section-execution");
 * sections = [...sections].slice(1);
 * var res2 = sections.flatMap(section => {
 *     var args = section.querySelectorAll(".literallayout span");
 *     var len = args.length;
 *     var norms = e.querySelectorAll(".section")[2].querySelectorAll(".norm");
 *     if (!norms) return e.querySelectorAll(".section")[2];
 *     var match = [...norms].map(l => { var match = l.textContent.match(/\b(.*) = (\d+) \(/);
 *     if (!match) return e.querySelectorAll(".section")[2].textContent;
 *     var name = match[1];
 *     var opcode = match[2];
 *     return name.toUpperCase() + "(" + opcode + ", " + len + ")";
 *     });
 *     return match;
 * });
 * console.log(res2);
 *
 * </pre>
 */
public enum JvmOpcode {
	AALOAD(50, 1), AASTORE(83, 1), ACONST_NULL(1, 1), ALOAD(25, 2), ALOAD_0(42, 1), ALOAD_1(43, 1), ALOAD_2(44, 1),
	ALOAD_3(45, 1), ANEWARRAY(189, 3), ARETURN(176, 1), ARRAYLENGTH(190, 1), ASTORE(58, 2), ASTORE_0(75, 1),
	ASTORE_1(76, 1), ASTORE_2(77, 1), ASTORE_3(78, 1), ATHROW(191, 1), BALOAD(51, 1), BASTORE(84, 1), BIPUSH(16, 2),
	CALOAD(52, 1), CASTORE(85, 1), CHECKCAST(192, 3), D2F(144, 1), D2I(142, 1), D2L(143, 1), DADD(99, 1), DALOAD(49, 1),
	DASTORE(82, 1), DCMPG(152, 1), DCMPL(151, 1), DCONST_0(14, 1), DCONST_1(15, 1), DDIV(111, 1), DLOAD(24, 2),
	DLOAD_0(38, 1), DLOAD_1(39, 1), DLOAD_2(40, 1), DLOAD_3(41, 1), DMUL(107, 1), DNEG(119, 1), DREM(115, 1),
	DRETURN(175, 1), DSTORE(57, 2), DSTORE_0(71, 1), DSTORE_1(72, 1), DSTORE_2(73, 1), DSTORE_3(74, 1), DSUB(103, 1),
	DUP(89, 1), DUP_X1(90, 1), DUP_X2(91, 1), DUP2(92, 1), DUP2_X1(93, 1), DUP2_X2(94, 1), F2D(141, 1), F2I(139, 1),
	F2L(140, 1), FADD(98, 1), FALOAD(48, 1), FASTORE(81, 1), FCMPG(150, 1), FCMPL(149, 1), FCONST_0(11, 1),
	FCONST_1(12, 1), FCONST_2(13, 1), FDIV(110, 1), FLOAD(23, 2), FLOAD_0(34, 1), FLOAD_1(35, 1), FLOAD_2(36, 1),
	FLOAD_3(37, 1), FMUL(106, 1), FNEG(118, 1), FREM(114, 1), FRETURN(174, 1), FSTORE(56, 2), FSTORE_0(67, 1),
	FSTORE_1(68, 1), FSTORE_2(69, 1), FSTORE_3(70, 1), FSUB(102, 1), GETFIELD(180, 3), GETSTATIC(178, 3), GOTO(167, 3),
	GOTO_W(200, 5), I2B(145, 1), I2C(146, 1), I2D(135, 1), I2F(134, 1), I2L(133, 1), I2S(147, 1), IADD(96, 1),
	IALOAD(46, 1), IAND(126, 1), IASTORE(79, 1), ICONST_M1(2, 1), ICONST_0(3, 1), ICONST_1(4, 1), ICONST_2(5, 1),
	ICONST_3(6, 1), ICONST_4(7, 1), ICONST_5(8, 1), IDIV(108, 1), IF_ACMPEQ(165, 3), IF_ACMPNE(166, 3),
	IF_ICMPEQ(159, 3), IF_ICMPNE(160, 3), IF_ICMPLT(161, 3), IF_ICMPGE(162, 3), IF_ICMPGT(163, 3), IF_ICMPLE(164, 3),
	IFEQ(153, 3), IFNE(154, 3), IFLT(155, 3), IFGE(156, 3), IFGT(157, 3), IFLE(158, 3), IFNONNULL(199, 3),
	IFNULL(198, 3), IINC(132, 3), ILOAD(21, 2), ILOAD_0(26, 1), ILOAD_1(27, 1), ILOAD_2(28, 1), ILOAD_3(29, 1),
	IMUL(104, 1), INEG(116, 1), INSTANCEOF(193, 3), INVOKEDYNAMIC(186, 5), INVOKEINTERFACE(185, 5),
	INVOKESPECIAL(183, 3), INVOKESTATIC(184, 3), INVOKEVIRTUAL(182, 3), IOR(128, 1), IREM(112, 1), IRETURN(172, 1),
	ISHL(120, 1), ISHR(122, 1), ISTORE(54, 2), ISTORE_0(59, 1), ISTORE_1(60, 1), ISTORE_2(61, 1), ISTORE_3(62, 1),
	ISUB(100, 1), IUSHR(124, 1), IXOR(130, 1), JSR(168, 3), JSR_W(201, 5), L2D(138, 1), L2F(137, 1), L2I(136, 1),
	LADD(97, 1), LALOAD(47, 1), LAND(127, 1), LASTORE(80, 1), LCMP(148, 1), LCONST_0(9, 1), LCONST_1(10, 1), LDC(18, 2),
	LDC_W(19, 3), LDC2_W(20, 3), LDIV(109, 1), LLOAD(22, 2), LLOAD_0(30, 1), LLOAD_1(31, 1), LLOAD_2(32, 1),
	LLOAD_3(33, 1), LMUL(105, 1), LNEG(117, 1), LOOKUPSWITCH(171, 11), LOR(129, 1), LREM(113, 1), LRETURN(173, 1),
	LSHL(121, 1), LSHR(123, 1), LSTORE(55, 2), LSTORE_0(63, 1), LSTORE_1(64, 1), LSTORE_2(65, 1), LSTORE_3(66, 1),
	LSUB(101, 1), LUSHR(125, 1), LXOR(131, 1), MONITORENTER(194, 1), MONITOREXIT(195, 1), MULTIANEWARRAY(197, 4),
	NEW(187, 3), NEWARRAY(188, 2), NOP(0, 1), POP(87, 1), POP2(88, 1), PUTFIELD(181, 3), PUTSTATIC(179, 3), RET(169, 2),
	RETURN(177, 1), SALOAD(53, 1), SASTORE(86, 1), SIPUSH(17, 3), SWAP(95, 1), TABLESWITCH(170, 15);

	byte opcode;
	int len;

	private JvmOpcode(int opcode, int len) {
		this.opcode = (byte) opcode;
		this.len = len;
	}

	public static JvmOpcode find(byte opcode) {
		for (JvmOpcode code : JvmOpcode.values()) {
			if (code.opcode == opcode) {
				return code;
			}
		}
		return null;
	}

	/**
	 * @return number of bytes that this opcodes needs including the opcode byte and
	 *         arguments.
	 */
	public int getLen() {
		return len;
	}
}