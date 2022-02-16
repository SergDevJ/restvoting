<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="ru.ssk.restvoting.util.DishUtil"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/common.js" defer></script>
<script src="resources/js/menu.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<input type="hidden" name="restaurantId" id="restaurantId" value="${restaurantId}">

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="menu.today.title"/></h3>
        <button class="btn btn-primary" onclick="addMenuItem()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th><spring:message code="dish.name"/></th>
                <th><spring:message code="dish.weight"/></th>
                <th><spring:message code="menu.price"/></th>
                <th></th>
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
                <h4 class="modal-title"><spring:message code="menu.add"/></h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <input type="hidden" id="dishId" name="dishId">
                    <input type="hidden" id="date" name="date">
                    <div class="form-group">
                        <label for="dishList"><spring:message code="dish.caption"/></label>
                        <select id="dishList" name="dishList" class="form-control">
                            <%
                                for(Map.Entry<Integer, String> e : DishUtil.getDishList().entrySet()) {
                                    out.write("<option value=\"" + e.getKey() + "\">"+ e.getValue());
                                }
                            %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="price" class="col-form-label"><spring:message code="menu.price"/></label>
                        <input type="number" class="form-control" id="price" name="price"
                               placeholder="<spring:message code="menu.price"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="saveMenuItem()">
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