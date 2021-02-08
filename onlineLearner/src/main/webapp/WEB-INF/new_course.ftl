<html>
<head><title>Kurs erstellen</title>

<body>
<h1>Kurs erstellen</h1>
<form action="new_course" method="post">
    <h3>Name <input type="text" name="courseName"></h3>
    <h3>Einschreibeschlüssel <input type="text" name="coursePassword"></h3>
    <h3>Anzahl freier Plätze <input type="number" name="courseFreeplace" min="1" max="100" step="1"></h3>
    <h3>Beschreibungstext <textarea id="courseDescription" name="courseDescription" rows="7" cols="40"></textarea></h3>
    <button type="submit">Erstellen</button>
</form>
</body>
</html>