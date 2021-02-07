<html>
<head><title>Welcome to Course ${course.name}</title>

<body>
    <h1> ${course.name}</h1>
    <form action="new_enroll" method="post">
    <h2> Einschreibeschlüssel <input type="password" name="password"><input type="hidden" name="kid" value=${course.kid}></h2>
    <button type="submit">Einschreiben</button>



</body>
</html>