<html>
<head><title>Welcome To Online Learner!</title>

<body>
    <h1> Online Learner </h1>
    <h3>Log In / Register </h3>
  <form name="user" action="hello" method="post">
    Name: <input type="text" name="name" /> <br/>
    Email: <input type="text" name="email" /> <br/>
    <input type="submit" value="Save" />
  </form>

  <table class="datatable">
    <tr>
        <th>Firstname</th>  <th>Lastname</th>
    </tr>
    <#list users as user>
    <tr>
        <td>${user.firstname}</td> <td>${user.lastname}</td>
    </tr>
    </#list>
  </table>
</body>
</html>