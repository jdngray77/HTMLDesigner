package antlr;
// Generated from /Volumes/The Vault/Workspaces/lang/src/main/antlr/lang.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link langParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface langVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link langParser#assignvar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignvar(langParser.AssignvarContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(langParser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(langParser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(langParser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(langParser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceDeclaration(langParser.ClassOrInterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classOrInterfaceModifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceModifiers(langParser.ClassOrInterfaceModifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceModifier(langParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#modifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifiers(langParser.ModifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(langParser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#normalClassDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalClassDeclaration(langParser.NormalClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameters(langParser.TypeParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameter(langParser.TypeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeBound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBound(langParser.TypeBoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(langParser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumBody(langParser.EnumBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumConstants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstants(langParser.EnumConstantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstant(langParser.EnumConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumBodyDeclarations(langParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDeclaration(langParser.InterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#normalInterfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalInterfaceDeclaration(langParser.NormalInterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(langParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(langParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(langParser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyDeclaration(langParser.ClassBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#memberDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDecl(langParser.MemberDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(langParser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#genericMethodOrConstructorDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericMethodOrConstructorDecl(langParser.GenericMethodOrConstructorDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#genericMethodOrConstructorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericMethodOrConstructorRest(langParser.GenericMethodOrConstructorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(langParser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(langParser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBodyDeclaration(langParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMemberDecl(langParser.InterfaceMemberDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceMethodOrFieldDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodOrFieldDecl(langParser.InterfaceMethodOrFieldDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceMethodOrFieldRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodOrFieldRest(langParser.InterfaceMethodOrFieldRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#methodDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaratorRest(langParser.MethodDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#voidMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidMethodDeclaratorRest(langParser.VoidMethodDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodDeclaratorRest(langParser.InterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#interfaceGenericMethodDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceGenericMethodDecl(langParser.InterfaceGenericMethodDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#voidInterfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidInterfaceMethodDeclaratorRest(langParser.VoidInterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constructorDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaratorRest(langParser.ConstructorDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constantDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclarator(langParser.ConstantDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableDeclarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarators(langParser.VariableDeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(langParser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constantDeclaratorsRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclaratorsRest(langParser.ConstantDeclaratorsRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constantDeclaratorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclaratorRest(langParser.ConstantDeclaratorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaratorId(langParser.VariableDeclaratorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(langParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(langParser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(langParser.ModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#packageOrTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageOrTypeName(langParser.PackageOrTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enumConstantName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstantName(langParser.EnumConstantNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeName(langParser.TypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(langParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceType(langParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(langParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableModifier(langParser.VariableModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArguments(langParser.TypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgument(langParser.TypeArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedNameList(langParser.QualifiedNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(langParser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#formalParameterDecls}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterDecls(langParser.FormalParameterDeclsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#formalParameterDeclsRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterDeclsRest(langParser.FormalParameterDeclsRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#methodBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBody(langParser.MethodBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constructorBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorBody(langParser.ConstructorBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(langParser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(langParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(langParser.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(langParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationName(langParser.AnnotationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#elementValuePairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePairs(langParser.ElementValuePairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#elementValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePair(langParser.ElementValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#elementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValue(langParser.ElementValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValueArrayInitializer(langParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeDeclaration(langParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeBody(langParser.AnnotationTypeBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeElementDeclaration(langParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeElementRest(langParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationMethodOrConstantRest(langParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationMethodRest(langParser.AnnotationMethodRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationConstantRest(langParser.AnnotationConstantRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#defaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultValue(langParser.DefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(langParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(langParser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclarationStatement(langParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclaration(langParser.LocalVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#variableModifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableModifiers(langParser.VariableModifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(langParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#catches}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatches(langParser.CatchesContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(langParser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#catchType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchType(langParser.CatchTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#finallyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyBlock(langParser.FinallyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#resourceSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResourceSpecification(langParser.ResourceSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#resources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResources(langParser.ResourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#resource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource(langParser.ResourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(langParser.FormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#switchBlockStatementGroups}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroups(langParser.SwitchBlockStatementGroupsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroup(langParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(langParser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(langParser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(langParser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#enhancedForControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnhancedForControl(langParser.EnhancedForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(langParser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(langParser.ParExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(langParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#statementExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpression(langParser.StatementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(langParser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(langParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(langParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#conditionalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(langParser.ConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalOrExpression(langParser.ConditionalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalAndExpression(langParser.ConditionalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveOrExpression(langParser.InclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveOrExpression(langParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(langParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(langParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#instanceOfExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceOfExpression(langParser.InstanceOfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(langParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#relationalOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalOp(langParser.RelationalOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpression(langParser.ShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#shiftOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOp(langParser.ShiftOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(langParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(langParser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpressionNotPlusMinus(langParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#castExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpression(langParser.CastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(langParser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#identifierSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierSuffix(langParser.IdentifierSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(langParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#createdName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedName(langParser.CreatedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#innerCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerCreator(langParser.InnerCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorRest(langParser.ArrayCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#classCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassCreatorRest(langParser.ClassCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGenericInvocation(langParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonWildcardTypeArguments(langParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgumentsOrDiamond(langParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonWildcardTypeArgumentsOrDiamond(langParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#selector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelector(langParser.SelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#superSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperSuffix(langParser.SuperSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGenericInvocationSuffix(langParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link langParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(langParser.ArgumentsContext ctx);
}