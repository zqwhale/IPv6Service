package mqeye.data.cv;

import mqeye.data.vo.Dsview;
import mqeye.service.serial.SensorReader;
import mqeye.service.tools.CrcHexUtil;

import org.apache.commons.lang.StringUtils;

class AMSElectricResult {
	private float phaseVol1 = 0;
	private float phaseVol2 = 0;
	private float phaseVol3 = 0;

	public void setPhaseVol1(float phaseVol1) {
		this.phaseVol1 = phaseVol1;
	}
	public void setPhaseVol2(float phaseVol2) {
		this.phaseVol2 = phaseVol2;
	}
	public void setPhaseVol3(float phaseVol3) {
		this.phaseVol3 = phaseVol3;
	}

	public float getPhaseVol(int idx) {
		float v = 0;
		switch(idx)
		{	case 1 : v = phaseVol1;break;
			case 2 : v = phaseVol2;break;
			case 3 : v = phaseVol3;break;
		}
		return v;
	}
	
	
	private float lineVol1 = 0;
	private float lineVol2 = 0;
	private float lineVol3 = 0;
	
	public void setLineVol1(float lineVol1) {
		this.lineVol1 = lineVol1;
	}
	public void setLineVol2(float lineVol2) {
		this.lineVol2 = lineVol2;
	}
	public void setLineVol3(float lineVol3) {
		this.lineVol3 = lineVol3;
	}
	public float getLineVol(int idx) {
		float v = 0;
		switch(idx)
		{	case 1 : v = lineVol1;break;
			case 2 : v = lineVol2;break;
			case 3 : v = lineVol3;break;
		}
		return v;
	}
	
	
	private float current1 = 0;
	private float current2 = 0;
	private float current3 = 0;
	
	public float getCurrent(int idx) {
		float c = 0;
		switch(idx)
		{	case 1 : c = current1;break;
			case 2 : c = current2;break;
			case 3 : c = current3;break;
		}
		return c;
	}
	public void setCurrent1(float current1) {
		this.current1 = current1;
	}
	public void setCurrent2(float current2) {
		this.current2 = current2;
	}
	public void setCurrent3(float current3) {
		this.current3 = current3;
	}
	
	public float acPower = 0; 	// you gong Power
	public float raPower = 0; 	// wu gong Power
	public float apPower = 0;	// shi zai Power
	
	
	public float getAcPower() {
		return acPower;
	}
	public void setAcPower(float acPower) {
		this.acPower = acPower;
	}
	public float getRaPower() {
		return raPower;
	}
	public void setRaPower(float raPower) {
		this.raPower = raPower;
	}
	public float getApPower() {
		return apPower;
	}
	public void setApPower(float apPower) {
		this.apPower = apPower;
	}


	public float pFactor = 0;

	public float getpFactor() {
		return pFactor;
	}
	public void setpFactor(float pFactor) {
		this.pFactor = pFactor;
	}
	
	
	
}


class AMSElectricReader extends SensorReader{
	
	public static AMSElectricResult getElectricPV(byte[] result){
		return getElectricPV("unknow",result);
	}
	
	public static AMSElectricResult getElectricPV(String shortAddr , byte[] result){
		AMSElectricResult er = null ;
		if (checkPassByCRC(result)){
			
			byte[] values = result ;
			byte[] ua_b = new byte[]{values[3],values[4]};
			byte[] ub_b = new byte[]{values[5],values[6]};
			byte[] uc_b = new byte[]{values[7],values[8]};
			
			float ua = (float) (CrcHexUtil.Bytes2Integer(ua_b)/10.0);
			float ub = (float) (CrcHexUtil.Bytes2Integer(ub_b)/10.0);
			float uc = (float) (CrcHexUtil.Bytes2Integer(uc_b)/10.0);
			er = new AMSElectricResult();
			
			er.setPhaseVol1(ua);
			er.setPhaseVol2(ub);
			er.setPhaseVol3(uc);
		}
		return er;
	}
	
	public static AMSElectricResult getElectricLV(byte[] result){
		return getElectricLV("unknow",result);
	}
	
	public static AMSElectricResult getElectricLV(String shortAddr , byte[] result){
		AMSElectricResult er = null ;
		if (checkPassByCRC(result)){
			
			byte[] values = result ;
			byte[] uab_b = new byte[]{values[3],values[4]};
			byte[] ubc_b = new byte[]{values[5],values[6]};
			byte[] uca_b = new byte[]{values[7],values[8]};
			
			float uab = (float) (CrcHexUtil.Bytes2Integer(uab_b)/10.0);
			float ubc = (float) (CrcHexUtil.Bytes2Integer(ubc_b)/10.0);
			float uca = (float) (CrcHexUtil.Bytes2Integer(uca_b)/10.0);
			er = new AMSElectricResult();
			
			er.setLineVol1(uab);
			er.setLineVol2(ubc);
			er.setLineVol3(uca);
		}
		return er;
	}
	
	public static AMSElectricResult getElectricA(byte[] result){
		return getElectricA("unknow",result);
	}
	
	public static AMSElectricResult getElectricA(String shortAddr , byte[] result){
		AMSElectricResult er = null ;
		if (checkPassByCRC(result)){
			
			byte[] values = result ;
			byte[] ia_b = new byte[]{values[3],values[4]};
			byte[] ib_b = new byte[]{values[5],values[6]};
			byte[] ic_b = new byte[]{values[7],values[8]};
			
			float ia = (float) (CrcHexUtil.Bytes2Integer(ia_b)*0.001*40);
			float ib = (float) (CrcHexUtil.Bytes2Integer(ib_b)*0.001*40);
			float ic = (float) (CrcHexUtil.Bytes2Integer(ic_b)*0.001*40);
			er = new AMSElectricResult();
			
			er.setCurrent1(ia);
			er.setCurrent2(ib);
			er.setCurrent3(ic);
		}
		return er;
	}
	
	public static AMSElectricResult getElectricPower(byte[] result,int svtype){
		return getElectricPower("unknow",result,svtype);
	}
	
	public static AMSElectricResult getElectricPower(String shortAddr , byte[] result , int svtype){
		AMSElectricResult er = null ;
		if (checkPassByCRC(result)){
			
			byte[] values = result ;
			byte[] power_b = new byte[]{values[3],values[4]};
			
			float power = (float) (CrcHexUtil.Bytes2Integer(power_b) );
			er = new AMSElectricResult();
			
			switch (svtype)
			{	case 1: er.setAcPower(power*0.04f);break;
				case 2: er.setRaPower(power*0.04f);break;
				case 3: er.setApPower(power*0.04f);break;
				case 4: er.setpFactor(power*0.001f);break;
			}
		}
		return er;
	}
	
}



// Electric Sensor Convert
public class AMSElectricConvert extends AbstractConvert {
	public static final String SERVICE_SERSOR_PV = "SVXBBPV";
	public static final String SERVICE_SERSOR_LV = "SVXBBLV";
	public static final String SERVICE_SERSOR_A = "SVXBBA";
	public static final String SERVICE_SERSOR_YGP = "SVXBBYG";
	public static final String SERVICE_SERSOR_WGP = "SVXBBWG";
	public static final String SERVICE_SERSOR_SZP = "SVXBBSZ";
	public static final String SERVICE_SERSOR_GLY = "SVXBBGLY";
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
			byte[] result = CrcHexUtil.HexString2Bytes(value2);
		
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_PV)){
				AMSElectricResult er = AMSElectricReader.getElectricPV( result );
				if (er!=null) value1 = er.getPhaseVol(bit) + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_LV)){
				AMSElectricResult er = AMSElectricReader.getElectricLV( result );
				if (er!=null) value1 = er.getLineVol(bit) + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_A)){
				AMSElectricResult er = AMSElectricReader.getElectricA( result );
				if (er!=null) value1 = er.getCurrent(bit) + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_YGP)){
				AMSElectricResult er = AMSElectricReader.getElectricPower( result ,1);
				if (er!=null) value1 = er.getAcPower() + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_WGP)){
				AMSElectricResult er = AMSElectricReader.getElectricPower( result ,2);
				if (er!=null) value1 = er.getRaPower() + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_SZP)){
				AMSElectricResult er = AMSElectricReader.getElectricPower( result ,3);
				if (er!=null) value1 = er.getApPower() + "";
			}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),AMSElectricConvert.SERVICE_SERSOR_GLY)){
				AMSElectricResult er = AMSElectricReader.getElectricPower( result ,4);
				if (er!=null) value1 = er.getpFactor() + "";
			}
		}
		return value1;
	}

}

