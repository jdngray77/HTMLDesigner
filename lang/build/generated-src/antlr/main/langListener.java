// Generated from lang.g4 by ANTLR 4.9
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link langParser}.
 */
public interface langListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link langParser#assignvar}.
	 * @param ctx the parse tree
	 */
	void enterAssignvar(langParser.AssignvarContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#assignvar}.
	 * @param ctx the parse tree
	 */
	void exitAssignvar(langParser.AssignvarContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(langParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(langParser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(langParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(langParser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(langParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(langParser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(langParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(langParser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceDeclaration(langParser.ClassOrInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classOrInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceDeclaration(langParser.ClassOrInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classOrInterfaceModifiers}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifiers(langParser.ClassOrInterfaceModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classOrInterfaceModifiers}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifiers(langParser.ClassOrInterfaceModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifier(langParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifier(langParser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void enterModifiers(langParser.ModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void exitModifiers(langParser.ModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(langParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(langParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#normalClassDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNormalClassDeclaration(langParser.NormalClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#normalClassDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNormalClassDeclaration(langParser.NormalClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(langParser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(langParser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(langParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(langParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void enterTypeBound(langParser.TypeBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeBound}.
	 * @param ctx the parse tree
	 */
	void exitTypeBound(langParser.TypeBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(langParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(langParser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumBody}.
	 * @param ctx the parse tree
	 */
	void enterEnumBody(langParser.EnumBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumBody}.
	 * @param ctx the parse tree
	 */
	void exitEnumBody(langParser.EnumBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstants(langParser.EnumConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstants(langParser.EnumConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(langParser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(langParser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumBodyDeclarations(langParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumBodyDeclarations(langParser.EnumBodyDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(langParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(langParser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#normalInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterNormalInterfaceDeclaration(langParser.NormalInterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#normalInterfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitNormalInterfaceDeclaration(langParser.NormalInterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(langParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(langParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(langParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(langParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(langParser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(langParser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(langParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(langParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#memberDecl}.
	 * @param ctx the parse tree
	 */
	void enterMemberDecl(langParser.MemberDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#memberDecl}.
	 * @param ctx the parse tree
	 */
	void exitMemberDecl(langParser.MemberDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(langParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(langParser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#genericMethodOrConstructorDecl}.
	 * @param ctx the parse tree
	 */
	void enterGenericMethodOrConstructorDecl(langParser.GenericMethodOrConstructorDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#genericMethodOrConstructorDecl}.
	 * @param ctx the parse tree
	 */
	void exitGenericMethodOrConstructorDecl(langParser.GenericMethodOrConstructorDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#genericMethodOrConstructorRest}.
	 * @param ctx the parse tree
	 */
	void enterGenericMethodOrConstructorRest(langParser.GenericMethodOrConstructorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#genericMethodOrConstructorRest}.
	 * @param ctx the parse tree
	 */
	void exitGenericMethodOrConstructorRest(langParser.GenericMethodOrConstructorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(langParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(langParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(langParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(langParser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBodyDeclaration(langParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBodyDeclaration(langParser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDecl(langParser.InterfaceMemberDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceMemberDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDecl(langParser.InterfaceMemberDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceMethodOrFieldDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodOrFieldDecl(langParser.InterfaceMethodOrFieldDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceMethodOrFieldDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodOrFieldDecl(langParser.InterfaceMethodOrFieldDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceMethodOrFieldRest}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodOrFieldRest(langParser.InterfaceMethodOrFieldRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceMethodOrFieldRest}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodOrFieldRest(langParser.InterfaceMethodOrFieldRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#methodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaratorRest(langParser.MethodDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#methodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaratorRest(langParser.MethodDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#voidMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterVoidMethodDeclaratorRest(langParser.VoidMethodDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#voidMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitVoidMethodDeclaratorRest(langParser.VoidMethodDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaratorRest(langParser.InterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaratorRest(langParser.InterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#interfaceGenericMethodDecl}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceGenericMethodDecl(langParser.InterfaceGenericMethodDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#interfaceGenericMethodDecl}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceGenericMethodDecl(langParser.InterfaceGenericMethodDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#voidInterfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterVoidInterfaceMethodDeclaratorRest(langParser.VoidInterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#voidInterfaceMethodDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitVoidInterfaceMethodDeclaratorRest(langParser.VoidInterfaceMethodDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constructorDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaratorRest(langParser.ConstructorDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constructorDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaratorRest(langParser.ConstructorDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclarator(langParser.ConstantDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclarator(langParser.ConstantDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(langParser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(langParser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(langParser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(langParser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constantDeclaratorsRest}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclaratorsRest(langParser.ConstantDeclaratorsRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constantDeclaratorsRest}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclaratorsRest(langParser.ConstantDeclaratorsRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constantDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclaratorRest(langParser.ConstantDeclaratorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constantDeclaratorRest}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclaratorRest(langParser.ConstantDeclaratorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(langParser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(langParser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(langParser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(langParser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(langParser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(langParser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(langParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(langParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#packageOrTypeName}.
	 * @param ctx the parse tree
	 */
	void enterPackageOrTypeName(langParser.PackageOrTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#packageOrTypeName}.
	 * @param ctx the parse tree
	 */
	void exitPackageOrTypeName(langParser.PackageOrTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstantName(langParser.EnumConstantNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstantName(langParser.EnumConstantNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(langParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(langParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(langParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(langParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(langParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(langParser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(langParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(langParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(langParser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(langParser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(langParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(langParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(langParser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(langParser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedNameList(langParser.QualifiedNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedNameList(langParser.QualifiedNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(langParser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(langParser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#formalParameterDecls}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterDecls(langParser.FormalParameterDeclsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#formalParameterDecls}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterDecls(langParser.FormalParameterDeclsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#formalParameterDeclsRest}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterDeclsRest(langParser.FormalParameterDeclsRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#formalParameterDeclsRest}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterDeclsRest(langParser.FormalParameterDeclsRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(langParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(langParser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void enterConstructorBody(langParser.ConstructorBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void exitConstructorBody(langParser.ConstructorBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(langParser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(langParser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(langParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(langParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(langParser.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(langParser.AnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(langParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(langParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationName(langParser.AnnotationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationName}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationName(langParser.AnnotationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairs(langParser.ElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairs(langParser.ElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(langParser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(langParser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(langParser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(langParser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(langParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(langParser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeDeclaration(langParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeDeclaration(langParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeBody(langParser.AnnotationTypeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeBody(langParser.AnnotationTypeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementDeclaration(langParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementDeclaration(langParser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementRest(langParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementRest(langParser.AnnotationTypeElementRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodOrConstantRest(langParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodOrConstantRest(langParser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodRest(langParser.AnnotationMethodRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodRest(langParser.AnnotationMethodRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationConstantRest(langParser.AnnotationConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationConstantRest(langParser.AnnotationConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(langParser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(langParser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(langParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(langParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(langParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(langParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(langParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(langParser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(langParser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(langParser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#variableModifiers}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifiers(langParser.VariableModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#variableModifiers}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifiers(langParser.VariableModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(langParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(langParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#catches}.
	 * @param ctx the parse tree
	 */
	void enterCatches(langParser.CatchesContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#catches}.
	 * @param ctx the parse tree
	 */
	void exitCatches(langParser.CatchesContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(langParser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(langParser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(langParser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(langParser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(langParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(langParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterResourceSpecification(langParser.ResourceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitResourceSpecification(langParser.ResourceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#resources}.
	 * @param ctx the parse tree
	 */
	void enterResources(langParser.ResourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#resources}.
	 * @param ctx the parse tree
	 */
	void exitResources(langParser.ResourcesContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(langParser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(langParser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(langParser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(langParser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#switchBlockStatementGroups}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroups(langParser.SwitchBlockStatementGroupsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#switchBlockStatementGroups}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroups(langParser.SwitchBlockStatementGroupsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(langParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(langParser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(langParser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(langParser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(langParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(langParser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(langParser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(langParser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(langParser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(langParser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(langParser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(langParser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(langParser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(langParser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(langParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(langParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(langParser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(langParser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(langParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(langParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(langParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(langParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(langParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(langParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(langParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(langParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalOrExpression(langParser.ConditionalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#conditionalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalOrExpression(langParser.ConditionalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalAndExpression(langParser.ConditionalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#conditionalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalAndExpression(langParser.ConditionalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(langParser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(langParser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(langParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(langParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(langParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(langParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(langParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(langParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#instanceOfExpression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceOfExpression(langParser.InstanceOfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#instanceOfExpression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceOfExpression(langParser.InstanceOfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(langParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(langParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#relationalOp}.
	 * @param ctx the parse tree
	 */
	void enterRelationalOp(langParser.RelationalOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#relationalOp}.
	 * @param ctx the parse tree
	 */
	void exitRelationalOp(langParser.RelationalOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(langParser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(langParser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#shiftOp}.
	 * @param ctx the parse tree
	 */
	void enterShiftOp(langParser.ShiftOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#shiftOp}.
	 * @param ctx the parse tree
	 */
	void exitShiftOp(langParser.ShiftOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(langParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(langParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(langParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(langParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(langParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpressionNotPlusMinus(langParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#unaryExpressionNotPlusMinus}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpressionNotPlusMinus(langParser.UnaryExpressionNotPlusMinusContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(langParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(langParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(langParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(langParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#identifierSuffix}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierSuffix(langParser.IdentifierSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#identifierSuffix}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierSuffix(langParser.IdentifierSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(langParser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(langParser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(langParser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(langParser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void enterInnerCreator(langParser.InnerCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void exitInnerCreator(langParser.InnerCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(langParser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(langParser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(langParser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(langParser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocation(langParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocation(langParser.ExplicitGenericInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArguments(langParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArguments(langParser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentsOrDiamond(langParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentsOrDiamond(langParser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArgumentsOrDiamond(langParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArgumentsOrDiamond(langParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSelector(langParser.SelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSelector(langParser.SelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSuperSuffix(langParser.SuperSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSuperSuffix(langParser.SuperSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocationSuffix(langParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocationSuffix(langParser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link langParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(langParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link langParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(langParser.ArgumentsContext ctx);
}