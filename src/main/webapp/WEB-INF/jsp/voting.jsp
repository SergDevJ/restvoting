<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script src="resources/js/common.js" defer></script>
<script src="resources/js/voting.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>


<div class="jumbotron pt-4">
  <div class="container">
    <h3 class="text-center"><spring:message code="restaurant.voting"/></h3>
    <button class="btn btn-primary" onclick="castVote()" id="voteBtn">
      <span class="fa fa-check"></span>
      <spring:message code="restaurant.vote"/>
    </button>
    <input type="hidden" name="votedId" id="votedId" value="0">
    <table class="table table-striped" id="datatable">
      <thead>
      <tr>
        <th></th>
        <th><spring:message code="restaurant.name"/></th>
        <th><spring:message code="restaurant.email"/></th>
        <th><spring:message code="restaurant.address"/></th>
        <th><spring:message code="restaurant.menu"/></th>
      </tr>
      </thead>
      <c:forEach items="${restaurants}" var="restaurant">
        <jsp:useBean id="restaurant" type="ru.ssk.restvoting.to.RestaurantVoteTo"/>
        <tr>
          <input type="hidden" name="id" value="${restaurant.id}">
          <td><input type="radio" name="vote" onclick="enableVoteButton();" value="${restaurant.id}" <c:if test="${restaurant.voted}">checked</c:if>/> </td>
          <td><c:out value="${restaurant.name}"/></td>
          <td><a href="mailto:${restaurant.email}">${restaurant.email}</a></td>
          <td>${restaurant.address}</td>
          <td><span>
            <a onclick=showMenu(${restaurant.id})> <span class="fa fa-bars"></span> </a>
          </span></td>
        </tr>
      </c:forEach>
    </table>
  </div>
</div>

<div class="modal fade" tabindex="-1" id="showMenu">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title"> <spring:message code="menu.voting.title"/> </h4>
        <h4 class="modal-static" id="restaurantName"> </h4>
        <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
      </div>
      <div class="modal-body">
        <form id="menuForm">

          <table style="margin-top: 10px" class="table">
            <thead class="thead-dark">
            <tr>
              <th scope="col">#</th>
              <th scope="col"><spring:message code="menu.name"/></th>
              <th scope="col"><spring:message code="dish.weight"/></th>
              <th scope="col"><spring:message code="menu.price"/></th>
            </tr>
            </thead>
            <tbody id="menuTable">
            </tbody>
          </table>
        </form>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" tabindex="-1" id="showNoMenu">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title"> <spring:message code="menu.voting.title"/> </h4>
        <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
      </div>
      <div class="modal-body">
        <form id="noMenuForm">
          <h6 class="modal-static"> <spring:message code="menu.voting.noMenu"/> </h6>
        </form>
      </div>
    </div>
  </div>
</div>

</body>

<script type="text/javascript">
    var i18n = [];
    i18n['voting.saved'] = '<spring:message code="voting.saved"/>';
</script>

</html>