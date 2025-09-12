<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar novo Curso</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
</head>
<body>

<div class="container">
    <section class="panel panel-primary vertical-space">
        <div class="panel-heading">
            <h1>Cadastrar novo Curso</h1>
        </div>

        <form:form modelAttribute="newCourseForm" cssClass="form-horizontal panel-body" action="/admin/course/new" method="post">

            <%-- Exibe mensagens de erro de validação --%>
            <form:errors path="*" cssClass="alert alert-danger" element="div" />

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCourse-name">Nome:</label>
                    <form:input path="name" id="newCourse-name" cssClass="form-control" required="required"/>
                    <form:errors path="name" cssClass="text-danger" />
                </div>
            </div>

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCourse-code">Código:</label>
                    <form:input path="code" id="newCourse-code" cssClass="form-control" required="required"/>
                    <form:errors path="code" cssClass="text-danger" />
                </div>
            </div>

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCourse-instructor">Instrutor:</label>
                    <form:select path="instructorId" id="newCourse-instructor" cssClass="form-control" required="required">
                        <form:option value="" label="Selecione um instrutor" />
                        <form:options items="${instructors}" itemValue="id" itemLabel="label" />
                    </form:select>
                    <form:errors path="instructorId" cssClass="text-danger" />
                </div>
            </div>

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCourse-category">Categoria:</label>
                    <form:select path="categoryId" id="newCourse-category" cssClass="form-control" required="required">
                        <form:option value="" label="Selecione uma categoria" />
                        <form:options items="${categories}" itemValue="id" itemLabel="label" />
                    </form:select>
                    <form:errors path="categoryId" cssClass="text-danger" />
                </div>
            </div>

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCourse-description">Descrição:</label>
                    <form:textarea path="description" id="newCourse-description" cssClass="form-control" rows="5"/>
                    <form:errors path="description" cssClass="text-danger" />
                </div>
            </div>

            <input class="btn btn-success submit" type="submit" value="Salvar"/>
        </form:form>
    </section>
</div>

</body>
</html>
