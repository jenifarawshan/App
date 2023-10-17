
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="DTO.SalesDTO" %>
<%@ page import="DTO.ProductDTO" %>

<%
    List<SalesDTO> salesList = (List<SalesDTO>) request.getAttribute("salesList");
    List<ProductDTO> productList = (List<ProductDTO>) request.getAttribute("productList");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Sales Management</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            h1 {
                margin-bottom: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            table, th, td {
                border: 1px solid black;
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            form {
                margin-bottom: 20px;
            }
            label {
                margin-right: 10px;
            }
            select, input[type='number'] {
                padding: 10px;
                border: 1px solid #ccc;
                box-sizing: border-box;
                margin-bottom: 10px;
            }
            input[type='submit'] {
                padding: 10px 20px;
                background-color: #4CAF50;
                color: white;
                border: none;
                cursor: pointer;
            }
            input[type='submit']:hover {
                background-color: #45a049;
            }
            .error-message {
                color: red;
                font-weight: bold;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <h1>Sales Data for Today</h1>
        <table>
            <tr>
                <th>Product Name</th>
                <th>Quantity</th>
            </tr>
            <%
                for (SalesDTO sales : salesList) {
            %>
                <tr>
                    <td><%= sales.getProductName() %></td>
                    <td><%= sales.getQuantity() %></td>
                </tr>
            <%
                }
            %>
        </table>

        <h2>Add Sales Data</h2>
        <p>売上日: ${currentDate}</p>
        
        <form action="${pageContext.request.contextPath}/sales" method="post">
            Product Code:
            <select name="productCode" required>
                <%
                    if (productList == null || productList.isEmpty()) {
                        out.println("<option value='' disabled selected>No product available</option>");
                    } else {
                        for (ProductDTO product : productList) {
                            out.println("<option value=\"" + product.getProductCode() + "\">" + product.getProductName() + "</option>");
                        }
                    }
                %>
            </select><br>
            Quantity: <input type="number" name="quantity" value=1 min=1 required><br>
            <input type="submit" value="Add Sales">
        </form>
        
        <button class="btn btn-lg btn-primary" id="submit" onclick="location.href ='products'" >製品リスト</button>


        <% if (errorMessage != null) { %>
            <p class="error-message"><%= errorMessage %></p>
        <% } %>
    </body>
</html>