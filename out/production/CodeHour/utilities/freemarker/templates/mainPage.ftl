<html>
<head>
    <title>${title}</title>
</head>
<body>
<h1>${title}</h1>

<h2>Clase</h2>

<ul>
    <#list studyGroups as studyGroup>
        <li><a href=${studyGroupLinks[studyGroup?index]}>${studyGroup}</a></li>
    </#list>
</ul>

<h2>Profesori</h2>

<ul>
    <#list teachers as teacher>
        <li><a href=${teacherLinks[teacher?index]}>${teacher}</a></li>
    </#list>
</ul>

<h2>Săli</h2>

<ul>
    <#list classrooms as classroom>
        <li><a href=${classroomLinks[classroom?index]}>${classroom}</a></li>
    </#list>
</ul>

</body>
</html>