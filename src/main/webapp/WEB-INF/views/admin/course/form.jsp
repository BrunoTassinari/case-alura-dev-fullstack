<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tag" %>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <title>${!empty courseForm.id ? 'Editar Curso' : 'Cadastrar novo Curso'}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
    </head>
    <body>
        <c:url value="${empty courseForm.id ? '/admin/course/new' : '/admin/course/edit/'.concat(courseForm.id)}" var="formAction"/>

        <tag:header />
        <div class="container">
            <section class="panel panel-primary vertical-space">
                <div class="panel-heading">
                    <h1>${!empty courseForm.id ? 'Editar Curso' : 'Cadastrar novo Curso'}</h1>
                </div>

                <form:form modelAttribute="courseForm" cssClass="form-horizontal panel-body" action="${formAction}" method="post">

                    <%-- Exibe mensagens de erro de validação --%>
                    <form:errors path="*" cssClass="alert alert-danger" element="div" />

                    <div class="row form-group">
                        <div class="col-md-9">
                            <label for="courseForm-name">Nome:</label>
                            <form:input path="name" id="courseForm-name" cssClass="form-control" required="required"/>
                            <form:errors path="name" cssClass="text-danger" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-9">
                            <label for="courseForm-code">Código:</label>
                            <form:input path="code" id="courseForm-code" cssClass="form-control" required="required"/>
                            <form:errors path="code" cssClass="text-danger" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-9">
                            <label for="courseForm-instructor">Instrutor:</label>
                            <form:select path="instructorId" id="courseForm-instructor" cssClass="form-control" required="required">
                                <form:option value="" label="Selecione um instrutor" />
                                <form:options items="${instructors}" itemValue="id" itemLabel="label" />
                            </form:select>
                            <form:errors path="instructorId" cssClass="text-danger" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-9">
                            <label for="courseForm-category">Categoria:</label>
                            <form:select path="categoryId" id="courseForm-category" cssClass="form-control" required="required">
                                <form:option value="" label="Selecione uma categoria" />
                                <form:options items="${categories}" itemValue="id" itemLabel="label" />
                            </form:select>
                            <form:errors path="categoryId" cssClass="text-danger" />
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-9">
                            <label for="courseForm-description">Descrição:</label>
                            <form:textarea path="description" id="courseForm-description" cssClass="form-control" rows="5"/>
                            <form:errors path="description" cssClass="text-danger" />
                        </div>
                    </div>

                    <c:if test="${!empty courseForm.id}">
                        <div class="row form-group">
                            <div class="col-md-9">
                                <div class="form-check">
                                    <form:checkbox path="active" id="courseForm-active" cssClass="form-check-input" />
                                    <label class="form-check-label" for="courseForm-active">Ativo</label>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <input class="btn btn-success submit" type="submit" value="${!empty courseForm.id ? 'Atualizar' : 'Salvar'}"/>
                </form:form>
            </section>
        </div>
    </body>
</html>
