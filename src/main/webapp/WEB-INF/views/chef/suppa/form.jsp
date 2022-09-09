<%--
- form.jsp
-
- Copyright (C) 2012-2022 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
		<jstl:if test="${command != 'create'}">
			<acme:input-textbox code="chef.suppa.form.label.code" path="code" readonly="true" />
		</jstl:if>			
		<acme:input-textbox code="chef.suppa.form.label.title" path="title" />
		<acme:input-moment code="chef.suppa.form.label.creation-moment" path="instantiationMoment" readonly="true" />
		<acme:input-textbox code="chef.suppa.form.label.description" path="description" />
		<acme:input-moment code="chef.suppa.form.label.start-date" path="startDate" />
		<acme:input-moment code="chef.suppa.form.label.finish-date" path="finishDate" />
		<acme:input-money code="chef.suppa.form.label.budget" path="budget" />
		<jstl:if test="${command == 'show'}">
			<acme:input-money code="chef.suppa.form.label.convertedBudget" path="budgetConverted" readonly="true" />
		</jstl:if>
		<acme:input-url code="chef.suppa.form.label.link" path="link" />
		<acme:input-url code="chef.suppa.form.label.kitchenware-code" path="kitchenwareCode" />
		<jstl:if test="${command == 'show'}">
			<acme:input-url code="chef.suppa.form.label.kitchenware" path="kitchenwareName" readonly="true"/>
			<acme:input-select code="chef.suppa.form.label.kitchenware.wareType" path="wareType" readonly="true" >
				<acme:input-option code="chef.suppa.form.label.kitchenware.ingredient" value="${IngredientType}" selected="${wareType == 'INGREDIENT'}"/>
				<acme:input-option code="chef.suppa.form.label.kitchenware.kitchen-utensil" value="${KitchenUtensilType}" selected="${wareType == 'KITCHEN_UTENSIL'}" />
			</acme:input-select>
		</jstl:if>
		
		<jstl:if test="${command != 'create'}">
			<acme:submit code="chef.suppa.form.submit.update" action="/chef/suppa/update"/>
			<acme:submit code="chef.suppa.form.submit.delete" action="/chef/suppa/delete"/>
		</jstl:if>
		
		<jstl:if test="${command == 'create'}">
		<acme:submit code="chef.suppa.form.submit.create" action="/chef/suppa/create"/>
	</jstl:if>
			
</acme:form>