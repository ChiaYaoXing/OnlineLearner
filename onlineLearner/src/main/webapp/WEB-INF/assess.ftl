<html>
<head><title>Welcome To Online Learner!</title>

<body>
    <p> Aufgabe: ${assignment.name} </p>
    <p> Beschreibung: ${assignment.description} </p>
    <p>Abgabetext: ${submission.text}</p>
  <form action="assess" method="post">
  <input type="hidden" name = "sid" value=${submission.sid}>
    <input type="hidden" name = "kid" value=${assignment.kid}>
    Bewertungsnote: <input type="radio" name="note" value="1">1 <input type="radio" name="note" value="2">2
    <input type="radio" name="note" value="3">3 <input type="radio" name="note" value="4">4
    <input type="radio" name="note" value="5">5  <br/>
    Kommentar: <textarea name="comment" id="comment" rows="7" cols="40"></textarea> <br/>
    <input type="submit" value="Bewerten" />
  </form>


</body>
</html>