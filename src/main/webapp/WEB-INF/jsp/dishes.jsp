<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/common.js" defer></script>
<script src="resources/js/dishes.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
  <div class="container">
    <h3 class="text-center"><spring:message code="dish.title"/></h3>
    <button class="btn btn-primary" onclick="add()">
      <span class="fa fa-plus"></span>
      <spring:message code="common.add"/>
    </button>
    <table class="table table-striped" id="datatable">
      <thead>
      <tr>
        <th><spring:message code="dish.name"/></th>
        <th><spring:message code="dish.weight"/></th>
        <th></th>
        <th></th>
      </tr>
      </thead>
    </table>
  </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title"><spring:message code="dish.add"/></h4>
        <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
      </div>
      <div class="modal-body">
        <form id="detailsForm">
          <input type="hidden" id="id" name="id">

          <div class="form-group">
            <label for="name" class="col-form-label"><spring:message code="dish.name"/></label>
            <input type="text" class="form-control" id="name" name="name"
                   placeholder="<spring:message code="dish.name"/>">
          </div>
          <div class="form-group">
            <label for="weight" class="col-form-label"><spring:message code="dish.weight"/></label>
            <input type="text" class="form-control" id="weight" name="weight"
                   placeholder="<spring:message code="dish.weight"/>">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
          <span class="fa fa-close"></span>
          <spring:message code="common.cancel"/>
        </button>
        <button type="button" class="btn btn-primary" onclick="save()">
          <span class="fa fa-check"></span>
          <spring:message code="common.save"/>
        </button>
      </div>
    </div>
  </div>
</div>
</body>
<jsp:include page="fragments/i18n.jsp"/>
</html>