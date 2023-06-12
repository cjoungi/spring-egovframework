package kr.spring.ch11.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import kr.spring.ch11.controller.PageRank;

public class PageRanksView extends AbstractXlsView{

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//sheet 생성
		HSSFSheet sheet = createFirstSheet((HSSFWorkbook)workbook);
		//컬럼레이블 지정
		createColumnLabel(sheet);
		
		List<PageRank> pageRanks = (List<PageRank>)model.get("pageRanks");
		
		int rowNum = 1;//0은 항목(레이블)을 명시하기 위햐서 이미 사용
		for(PageRank rank:pageRanks) {
			createPageRankRow(sheet,rank,rowNum++);
		}
		String filename = "pageRanks2023.xls";
		response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
	}
	//쉬트 생성
	private HSSFSheet createFirstSheet(HSSFWorkbook workbook) {
		HSSFSheet sheet = workbook.createSheet();
					      //sheet index , 이름
		workbook.setSheetName(0, "페이지 순위");
						//column index, 이름
		sheet.setColumnWidth(1, 256*20);
		return sheet;
	}

	//컬럼 레이블 지정
	private void createColumnLabel(HSSFSheet sheet) {
		HSSFRow firstRow = sheet.createRow(0);
		HSSFCell cell = firstRow.createCell(0);
		cell.setCellValue("순위");
		
		cell = firstRow.createCell(1);
		cell.setCellValue("페이지");
	}
	
	//행단위로 데이터 표시
	private void createPageRankRow(HSSFSheet sheet,PageRank rank, int rowNum) {
		HSSFRow row = sheet.createRow(rowNum);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(rank.getRank());
		
		cell = row.createCell(1);
		cell.setCellValue(rank.getPage());
	}
}
