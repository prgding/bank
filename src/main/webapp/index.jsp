<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<title>index page</title>
		<style>
            h1 {
                text-align: center;
                padding-bottom: 200px;
            }

            table {
                margin: auto;
                line-height: 40px;
                text-align: center;
            }

            form {
                line-height: 40px;
                padding: 60px;
                text-align: center;
            }
		</style>
	</head>
	<body>

		<form action="transfer" method="post">
			<h1>模拟账户转账</h1>
			<table>
				<tr>
					<td>转出账户：</td>
					<td><label>
						<input id="from" name="from" type="text" value="act001"/>
					</label></td>
				</tr>
				<tr>
					<td>转入账户：</td>
					<td><label>
						<input id="to" name="to" type="text" value="act002"/>
					</label></td>
				</tr>
				<tr>
					<td>金额：</td>
					<td><label>
						<input id="money" name="money" type="text" value="10000"/>
					</label></td>
				</tr>
			</table>
			<label><input type="submit" name="submit" value="提交"></label>
			&emsp;
			<label><input type="button" id="clear" name="clear" value="清空"></label>
		</form>
		<script>
			document.getElementById("clear").onclick = function () {
				document.getElementById("from").value = ''
				document.getElementById("to").value = ''
				document.getElementById("money").value = ''
			}
		</script>
	</body>
</html>
