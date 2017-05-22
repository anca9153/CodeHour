<html>
<head>
    <title>${resourceId}</title>
    <title>Timetable</title>
    <style type="text/css">
    body
    {
        font-family: arial;
    }

    th,td
    {
        margin: 0;
        text-align: center
        border-collapse: collapse;
        outline: 1px solid #e3e3e3;
    }

    td
    {
        padding: 10px 10px;
    }

    th
    {
        background: #666;
        color: white;
        padding: 10px 10px;
    }

    </style>
</head>
<body>

<h2>Clasa ${resourceId}</h2>

<#assign tableRO = ["Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica"]>
<#assign currentTableDay = "none">
<#assign dayIndex = -1>

<table>
    <tr>
        <th>Interval</th>
        <th>Disciplina</th>
        <th>Profesor</th>
        <th>Sala</th>
    </tr>
    <#list events as event>
        <#if currentTableDay != event.time.getDay()>
            <#assign currentTableDay = event.time.getDay()>
            <#assign dayIndex = dayIndex + 1>
            <tr>
                <td>${tableRO[dayIndex]}</td>
            </tr>
        </#if>
        <tr>
            <td>${event.time.getHourInterval()}</td>
            <td>${event.description}</td>
            <td>
                <#list event.resources.getResources() as resource>
                    <#if resource.resourceType == "teacher">
                        ${resource.name}
                    </#if>
                </#list>
            </td>
            <td>
                <#list event.resources.getResources() as resource>
                    <#if resource.resourceType == "classroom">
                        ${resource.id}
                    </#if>
                </#list>
            </td>
        </tr>
    </#list>
</table>

</body>
</html>