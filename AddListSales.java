package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DAO.SalesDAO;
import DTO.ProductDTO;
import DTO.SalesDTO;
import utils.DB;

public class AddListSales extends HttpServlet {
    private SalesDAO salesDAO;
    private ProductDAO productDAO;

    // Constructor to initialize the SalesDAO
    public AddListSales() throws ClassNotFoundException, SQLException {
        Connection conn = DB.getConnection();
        salesDAO = new SalesDAO(conn);
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        // Implement logic to list sales for today and populate the JSP
        try {
            // Get the current date in the format "yyyy-MM-dd"
            String currentDate = java.time.LocalDate.now().toString();

            // Call listSales with the date parameter
            List<SalesDTO> salesList = salesDAO.listSales(currentDate);

            // Set the salesList in the request to be used in the JSP
            request.setAttribute("salesList", salesList);
            System.out.println("Sales list size: " + salesList.size());

            // Get the list of products from the ProductDAO ProductDAO
            List<ProductDTO> productList = productDAO.getProducts(null); // Pass null to get all products
            request.setAttribute("productList", productList);
            System.out.println("Product list size: " + productList.size());

        } catch (ClassNotFoundException | SQLException e) {
            // Handle any exceptions appropriately
            e.printStackTrace();
            request.setAttribute("errorMessage",
                    "An error occurred: " + e.getMessage());
        } finally {
            // Forward the request to the JSP page
            request.getRequestDispatcher("/WEB-INF/add_list_sales.jsp")
                    .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // Implement logic to add new sales data
        try {
            int productCode = Integer.parseInt(request.getParameter("productCode"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Get the current date in the format "yyyy-MM-dd"
            String currentDate = java.time.LocalDate.now().toString();

            // Use SalesDAO to add or update sales data
            if (salesDAO.addSales(currentDate, productCode, quantity)) {
                System.out.println("Sales added successfully.");
            } else {
                // If product code is invalid, handle the error
                System.out.println("Invalid product code.");
            }

            // Redirect to doGet to display the updated sales list
            request.getRequestDispatcher("/app/sales").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            // Handle any exceptions appropriately
            e.printStackTrace();
            request.setAttribute("errorMessage",
                    "An error occurred: " + e.getMessage());
        }
    }
}
