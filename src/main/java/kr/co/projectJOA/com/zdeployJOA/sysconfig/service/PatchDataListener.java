/**
 * 
 */
package kr.co.projectJOA.com.zdeployJOA.sysconfig.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.projectJOA.com.constant.GlobalConstant;
import kr.co.projectJOA.com.util.ResultMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;


/**
 * @author Administrator
 *
 */
public class PatchDataListener  implements ApplicationListener{

	@Autowired
	private SystemConfigService systemConfigService;
	
	public static int startcnt = 0;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		try{
			startcnt++;
			if(startcnt == 1){
				HashMap<String,Object> param = new HashMap<String,Object>();
				
				int isProcessTable = systemConfigService.isCheckPatchProcess(param);
				if(isProcessTable < 1){
					//GlobalConstant.IS_ABLE_PATCHPROCESS = false;
					systemConfigService.createPatchProcess(param); //패치이력 테이블 생성
				}
				
				if(GlobalConstant.IS_ABLE_PATCHPROCESS){
					
					List<ResultMap> patchHist = systemConfigService.getPatchHist(param);
					
	//				System.out.println("=========&&&&==============="+patchHist.size());
					
					String path = PatchDataListener.class.getResource("").getPath(); 
	//				System.out.println("====path="+path);
					if(path.startsWith("/")){
						path = path.substring(1);
					}
					int langIndex = path.indexOf("WEB-INF/classes");
					path = path.substring(0, langIndex+15)+"/"+"config/deployjoa_patch.json";
					
					JSONArray patchlst = getPatchData(path);
					
	        		for (int i = 0; i < patchlst.size(); i++) {
	        			JSONObject temp = (JSONObject) patchlst.get(i);
	        			
	        			Map tMap = temp;
	        			
	        			int vercnt = systemConfigService.getPatchVerCount(tMap);
	        			if(vercnt < 1){
	        				systemConfigService.createPatch(tMap);
	        			}
	        			
	        			System.out.println(tMap.toString());
	        			System.out.println("==vercnt=="+vercnt);
	        		}
				}
			}
			

			
		}catch(Exception e){
			//e.printStackTrace();
			System.err.println("error : ");
		}		
	}
	
	/**
	 * patch 내용을 가져와서 jsonobject로 변환한다.
	 * @param pathDataPath
	 * @return
	 * @throws Exception
	 */
	public JSONArray getPatchData(String pathDataPath) throws Exception{
		
		
		Object obj = (new FileReader(pathDataPath));

		JSONArray patchlst = null;
		
		StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(pathDataPath));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            if( sb != null && (sb.toString()).length() > 0 ){
                JSONObject jsonobj = JSONObject.fromObject(sb.toString());
                patchlst = jsonobj.getJSONArray("patchList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) try {br.close(); } catch (IOException e) {}
        }

        return patchlst;
	}

	public void setNewPatch(){
		

	}
	
}
