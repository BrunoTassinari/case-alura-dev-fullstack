<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html>
<head>
    <title>${empty categoryForm.id ? 'Cadastrar nova Categoria' : 'Editar Categoria'}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
</head>

<c:url value="${empty categoryForm.id ? '/admin/category/new' : '/admin/category/edit/'.concat(categoryForm.id)}" var="formAction"/>

<div class="container">
    <section class="panel panel-primary vertical-space">
        <div class="panel-heading">
            <h1>${empty categoryForm.id ? 'Cadastrar nova Categoria' : 'Editar Categoria'}</h1>
        </div>

        <form:form modelAttribute="categoryForm" cssClass="form-horizontal panel-body" action="${formAction}" method="post">

             <%-- Exibe mensagens de erro de validação --%>
             <form:errors path="*" cssClass="alert alert-danger" element="div" />

            <div class="row form-group">
                <div class="col-md-9">
                    <label for="categoryForm-name">Nome:</label>
                    <form:input path="name" id="categoryForm-name" cssClass="form-control" required="required"/>
                </div>

                <div class="col-md-9">
                    <label for="categoryForm-code">Código:</label>
                    <form:input path="code" id="categoryForm-code" cssClass="form-control" required="required" />
                </div>

                 <div class="col-md-9">
                    <label for="categoryForm-order">Ordem:</label>
                    <form:input path="order" type="number" min="1" id="categoryForm-order" cssClass="form-control" required="required"/>
                </div>

                <div class="col-md-9">
                    <label for="categoryForm-color">Cor:</label>
                    <form:input type="color" path="color" id="categoryForm-color" cssClass="form-control" style="width: 48px; padding: 0" required="required"/>
                </div>

            </div>

            <input class="btn btn-success submit" type="submit" value="${!empty categoryForm.id ? 'Atualizar' : 'Salvar'}"/>
        </form:form>
    </section>
</div>