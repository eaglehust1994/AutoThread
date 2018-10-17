package com.viettel.qll.rest;

import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.viettel.qll.business.TaskBusinessImpl;
import com.viettel.erp.dto.DataListDTO;
import com.viettel.qll.dto.TaskDTO;

/**
 * @author hailh10
 */
 
public class TaskRsServiceImpl implements TaskRsService {

	protected final Logger log = Logger.getLogger(TaskRsService.class);
	@Autowired
	TaskBusinessImpl taskBusinessImpl;
	

	
	@Override
	public Response doSearch(TaskDTO obj) {
		List<TaskDTO> ls = taskBusinessImpl.doSearch(obj);
		if (ls == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} else {
			DataListDTO data = new DataListDTO();
			data.setData(ls);
			data.setTotal(ls.size());
			data.setSize(ls.size());
			data.setStart(1);
			return Response.ok(data).build();
		}
	}
	
	
}
