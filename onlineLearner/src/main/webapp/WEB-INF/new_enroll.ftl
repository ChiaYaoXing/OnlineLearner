<html>
<head><title>Welcome to Course ${course.name}</title>

<body>
    <h1> ${course.name}</h1>
    <form action="new_enroll" method="post">
    ${res}<input type="hidden" name="kid" value=${course.kid}>
    <button type="submit">Einschreiben</button>



</body>
</html>