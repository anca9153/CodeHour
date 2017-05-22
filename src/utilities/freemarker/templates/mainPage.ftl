<html>
<head>
    <title>${title}</title>
    <style type="text/css">
        body{
            margin-left: 100px;
        }
        ul {
            list-style-type: none;
        }
        li {
            margin-bottom: 5px;
        }
        a{
            text-decoration: none;
        }
        a:hover{
            text-decoration: underline;
        }
    </style>
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

<h2>Orare generale</h2>

<ul>
    <li>
        <a href="studyGroupGeneral.html">Clase</a>
    </li>
    <li>
        <a href="teacherGeneral.html">Profesori</a>
    </li>
    <li>
        <a href="classroomGeneral.html">Sali</a>
    </li>
</ul>

</body>
</html>