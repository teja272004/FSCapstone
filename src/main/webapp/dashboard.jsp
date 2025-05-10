<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            text-align: center;
        }

        .container {
            background-color: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            margin-bottom: 20px;
            color: #007BFF;
        }

        p {
            font-size: 18px;
        }

        .logout-btn {
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #d9534f; /* Bootstrap danger color */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }

        .logout-btn:hover {
            background-color: #c9302c; /* Darker red on hover */
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome!</h1>
        <p>YO! Welcome Man</p>
        
        <form action="LogoutServlet" method="post">
            <input type="hidden" name="username" value="${sessionScope.username != null ? sessionScope.username : ''}"> <!-- Hidden field for username -->
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
</body>
</html>
