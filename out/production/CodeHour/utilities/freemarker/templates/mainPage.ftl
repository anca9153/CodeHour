<html>
<head>
    <title>${title}</title>
</head>
<body>
<h1>${title}</h1>

<h2>Clase</h2>

<ul>
    <#list studyGroups as studyGroup>
        <li>${studyGroup}</li>
    </#list>
</ul>

<h2>Profesori</h2>

<ul>
    <#list teachers as teacher>
        <li>${teacher}</li>
    </#list>
</ul>

<h2>Săli</h2>

<ul>
    <#list classrooms as classroom>
        <li>${classroom}</li>
    </#list>
</ul>

</body>
</html>