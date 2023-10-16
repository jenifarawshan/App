package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.SalesDTO;

public class SalesDAO {
    private Connection connection;

    public SalesDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean addSales(String date, int productCode, int quantity) throws SQLException {
        String query = "SELECT product_name from m_product WHERE product_code = ? AND delete_datetime IS NULL";
        
     

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setInt(1, productCode);
        ResultSet rs = preparedStatement.executeQuery();
        
        if(rs.next()) {
        	PreparedStatement ps = connection.prepareStatement("INSERT INTO t_sales(sales_date, product_code, quantity, register_datetime, update_datetime) values(?, ?, ?, NOW(), NOW())");
    		
        	ps.setString(1, date);
        	ps.setInt(2, productCode);
        	ps.setInt(3, quantity);
        	ps.executeUpdate();
        	return true;
        }
        else {
        	return false;
        }
        
    }
    
    
    public void updateSales(String date, int productCode, int quantity) throws SQLException {
    	PreparedStatement preparedStatement = connection.prepareStatement("UPDATE t_sales SET quantity=?, update_datetime=NOW() WHERE sales_date=? AND product_code=?");
    	
    	preparedStatement.setInt(1, quantity);
    	preparedStatement.setString(2, date);
    	preparedStatement.setInt(3, productCode);
    	
    	preparedStatement.executeUpdate();
    }
    
    

    public  List<SalesDTO> listSales(String date) throws SQLException{
    	List<SalesDTO> salesList = new ArrayList<SalesDTO>();
    	PreparedStatement preparedStatement  = null;
    	
    	preparedStatement = connection.prepareStatement("SELECT p.product_name, s.quantity FROM m_product p INNER JOIN t_sales s ON p.product_code = s.product_code WHERE s.sales_date=?");
    	preparedStatement.setString(1, date);
    	
    	ResultSet rs = preparedStatement.executeQuery();
    	
    	while(rs.next()) {
    		SalesDTO sales = new SalesDTO();
    		sales.setProductName(rs.getString("product_name"));
    		sales.setQuantity(rs.getInt("quantity"));
    		salesList.add(sales);
    	}
    	return salesList;
    }

}