package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	//멤버변수 설정
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	
	//데이터베이스 연결 메서드 정의
	// 예외처리는 Exception 메서드를 호출하는 곳에서 하기 위해 throws 사용 
	
	public Connection getConnection() throws Exception{
		// Database Connection Pool : 미리 서버에서 연결하고 연결정보를 저장
		//					 필요할때 연결자원의 이름을 불러서 사용
		// 수정 1회 최소화, 
		
		//  context.xml 불러오기 위해 객체생성  import javax.naming.Context;
		Context init=new InitialContext();
		// 자원이름 찾아서 불러오기 javax.sql.DataSource
		DataSource ds=(DataSource)init.lookup("java:comp/env/jdbc/MysqlDB");
		// 디비연결
		con=ds.getConnection();
		return con;
	}
	
	
	// 예외 상관없이 마무리 작업(sql 작업이 끝나면 기억장소 정리)
	public void dbClose() {
		if(rs!=null) {
			try {
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		if(con!=null) {
			try {
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	// 회원가입 - insertMember 메서드 정의, 변수는 MemberDTO mDTO
	public void insertMember(MemberDTO mDTO) {
		try {
			// 드라이버 불러오기, 데이터베이스 연결
			con=getConnection();
			
			// sql 구문
			String sql="insert into member(id,pass,name,date,email,zipno,address,phone,mobile) values(?,?,?,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, mDTO.getId());
			pstmt.setString(2, mDTO.getPass());
			pstmt.setString(3, mDTO.getName());
			pstmt.setTimestamp(4, mDTO.getDate());
			pstmt.setString(5, mDTO.getEmail());
			pstmt.setString(6, mDTO.getZipno());
			pstmt.setString(7, mDTO.getAddress());
			pstmt.setString(8, mDTO.getPhone());
			pstmt.setString(9, mDTO.getMobile());
			
			//실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
	}
	
	
	// 로그인 - userCheck 메서드 정의, 리턴형 MemberDTO, 변수는 id/pass
	public MemberDTO userCheck(String id, String pass) {
		MemberDTO mDTO = null;
		try {
			//드라이버 불러오기, 데이터베이스 연결
			con=getConnection();
			
			//sql구문 만들기 - id와 pass가 일치하는 회원
			String sql = "select * from member where id=? and pass=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pass);
			
			//실행
			rs=pstmt.executeQuery();
			
			//데이터베이스에 접근하여 일치하는 값이 있으면 MemberDTO에 값을 저장한다
			if(rs.next()) {
				//접근하여 일치하는 값이 있으면(true) MemberDTO 객체를 생성
				mDTO = new MemberDTO();
				
				//mDTO에 데이터베이스에서 가져온 값을 저장한다
				mDTO.setId(rs.getString("id"));
				mDTO.setPass(rs.getString("pass"));
				mDTO.setName(rs.getString("name"));
				mDTO.setDate(rs.getTimestamp("date"));
				mDTO.setZipno(rs.getString("zipno"));
				mDTO.setAddress(rs.getString("address"));
				mDTO.setEmail(rs.getString("email"));
				mDTO.setPhone(rs.getString("phone"));
				mDTO.setMobile(rs.getString("mobile"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return mDTO;
	}
	
	
	//회원정보조회 - getMember(변수 id) 메서드 정의 리턴형 MemberDTO
	public MemberDTO getMember(String id) {
		MemberDTO mDTO = null;
		try {
			con=getConnection();
			
			String sql = "select * from member where id=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				mDTO = new MemberDTO();
				mDTO.setId(rs.getString("id"));
				mDTO.setName(rs.getString("name"));
				mDTO.setEmail(rs.getString("email"));
				mDTO.setZipno(rs.getString("zipno"));
				mDTO.setAddress(rs.getString("address"));
				mDTO.setPhone(rs.getString("phone"));
				mDTO.setMobile(rs.getString("mobile"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return mDTO;
	}
	
	
	//회원정보 수정 - updateDTO를 변수로 받는다
	public void updateMember(MemberDTO updateDTO) {
		try {
			con=getConnection();
			
			String sql = "update member set name=?,email=?, zipno=?, address=?,phone=?,mobile=? where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, updateDTO.getName());
			pstmt.setString(2, updateDTO.getEmail());
			pstmt.setString(3, updateDTO.getZipno());
			pstmt.setString(4, updateDTO.getAddress());
			pstmt.setString(5, updateDTO.getPhone());
			pstmt.setString(6, updateDTO.getMobile());
			pstmt.setString(7, updateDTO.getId());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
	}
}
