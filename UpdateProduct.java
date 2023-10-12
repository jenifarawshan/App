package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.ProductDAO;
import DTO.ProductDTO;

public class UpdateProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        int productId = getProductIdFromPath(request);

        if (productId == -1) {
            response.sendRedirect(request.getContextPath() + "/app/products");
            return;
        }

        try {
            ProductDAO productDAO = new ProductDAO();
            ProductDTO existingProduct = productDAO.getProduct(productId);

            if (existingProduct != null) {
                request.setAttribute("product", existingProduct);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/update_product.jsp");
                dispatcher.forward(request, response);
            } else {
                response.getWriter().println("Product not found.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        int productId = getProductIdFromPath(request);

        if (productId == -1) {
            response.sendRedirect(request.getContextPath() + "/app/products");
            return;
        }

        String action = request.getParameter("action");

        if (action != null) {
            try {
                ProductDAO productDAO = new ProductDAO();
                ProductDTO existingProduct = productDAO.getProduct(productId); // Retrieve the existing product

                if (action.equals("update")) {
                    String productName = request.getParameter("productName");
                    String unitPriceStr = request.getParameter("unitPrice");

                    if (productName != null && unitPriceStr != null && !productName.isEmpty() && !unitPriceStr.isEmpty()) {

                        int unitPrice = Integer.parseInt(unitPriceStr);
                        existingProduct.setProductName(productName); // Update product name
                        existingProduct.setProductPrice(unitPrice); // Update product price
                        productDAO.updateProduct(existingProduct); // Update the product

                    }
                } 
                if (action.equals("delete")) {
//                    if (!productDAO.hasProductBeenSold(productId)) {
//                    	response.sendRedirect(request.getContextPath() + "/products");
//                    }
                    productDAO.deleteProduct(productId);
                }
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            } catch (SQLException | ClassNotFoundException e) {
                out.println("An error occurred: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/products/update/" + productId);
    }

    private int getProductIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            return -1;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length < 2) {
            return -1;
        }

        try {
//            return Integer.parseInt(pathParts[1]);
        	
        	
        	String productCodeString = pathParts[1].replaceFirst("^0+(?!$)", "");
        	return Integer.parseInt(productCodeString);
        	
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}