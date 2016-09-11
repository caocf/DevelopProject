package com.ebox.pub.boxctl;


import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.network.model.base.type.BoxInfoType;
import com.ebox.ex.network.model.enums.BoxSizeType;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.pub.service.global.GlobalField;

public class BoxUtils {
	// BoardNum表示格口箱编号, 01-16
	// BoxNum 表示格口编号, 顺序为从左至右，然后从上至下，依次为01-99
	// |01|06|
	// |02|07|
	// |03|08|
	// |04|09|
	// |05|10|
	
	// boxCode由5位组成
	// 第1 位表示格口箱相对于控制柜的位置，用字母表示；目前固定为"M"
	// 第2、3 位表示格口箱编号, 01-16
	// 第4、5 位表示格口序号，从最左边一列开始，从上往下数，然后右边的列依次从上往下数，01-99
	
	// M0102 对应的BoardNum为01 BoxNum为02
	
	public static BoxInfo getBoxByCode(String boxCode)
	{
		BoxInfo info = new BoxInfo();
		
		String boardStr = boxCode.substring(1, 3);
		info.setBoardNum(Integer.valueOf(boardStr));
		
		String boxStr = boxCode.substring(3, 5);
		info.setBoxNum(Integer.valueOf(boxStr));
		
		return info;
	}
	
	public static String generateBoxCode(int boardNum, int boxNum)
	{
		return "M"+String.format("%02d",boardNum)
				+String.format("%02d",boxNum);
	}
	
	/**
	 * 
	 * @return 0- 关闭 1- 打开
	 */
	public static Integer getBoxLocalState(BoxInfo boxInfo)
	{
		RackInfo rack;
		BoxInfo boxInList;
		
		if(GlobalField.boxInfoLocal == null)
		{
			return 0;
		}
		
		for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
		{
			rack = GlobalField.boxInfoLocal.get(i);
			if(rack.getBoardNum().equals(boxInfo.getBoardNum()))
			{
				for(int j= 0 ;  j < rack.getBoxes().length; j++)
				{
					boxInList = GlobalField.boxInfoLocal.get(i).getBoxes()[j];
					if(boxInList.getBoxNum().equals(boxInfo.getBoxNum()))
					{
						return boxInList.getDoorState();
					}
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @return 0- 关闭 1- 打开
	 */
	public static Integer getBoxLocalState(BoxInfoType box)
	{
		BoxInfo boxInfo = getBoxByCode(box.getBoxCode());
		RackInfo rack;
		BoxInfo boxInList;
		
		if(GlobalField.boxInfoLocal == null)
		{
			return DoorStatusType.unknow;
		}
		
		for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
		{
			rack = GlobalField.boxInfoLocal.get(i);
			if(rack.getBoardNum().equals(boxInfo.getBoardNum()))
			{
				for(int j= 0 ;  j < rack.getBoxes().length; j++)
				{
					boxInList = GlobalField.boxInfoLocal.get(i).getBoxes()[j];
					if(boxInList.getBoxNum().equals(boxInfo.getBoxNum()))
					{
						return boxInList.getDoorState();
					}
				}
			}
		}
		
		return DoorStatusType.unknow;
	}
	
	/**
	 * 
	 * @return 0- 成功-1- 失败
	 */
	public static Integer setBoxLocalState(BoxInfo boxInfo)
	{
		RackInfo rack;
		BoxInfo boxInList;
		
		if(GlobalField.boxInfoLocal == null)
		{
			return -1;
		}
		
		for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
		{
			rack = GlobalField.boxInfoLocal.get(i);
			if(rack.getBoardNum().equals(boxInfo.getBoardNum()))
			{
				for(int j= 0 ;  j < rack.getBoxes().length; j++)
				{
					boxInList = GlobalField.boxInfoLocal.get(i).getBoxes()[j];
					if(boxInList.getBoxNum().equals(boxInfo.getBoxNum()))
					{
						boxInList.setDoorState(boxInfo.getDoorState());
						return 0;
					}
				}
			}
		}
		
		return -1;
	}
	
	public static String getBoxSizeDesc(BoxInfoType box)
	{
		if(box.getBoxSize().equals(BoxSizeType.small))
		{
			return AppApplication.globalContext.getResources().getString(R.string.ex_little_box);
		}
		else if(box.getBoxSize().equals(BoxSizeType.medium))
		{
			return AppApplication.globalContext.getResources().getString(R.string.ex_middle_box);
		}
		else if(box.getBoxSize().equals(BoxSizeType.tiny))
		{
			return AppApplication.globalContext.getResources().getString(R.string.ex_tiny_box);
		}
		else
		{
			return AppApplication.globalContext.getResources().getString(R.string.ex_big_box);
		}
	}
	

	public static String getBoxDesc(BoxInfo boxInfo)
	{
		return String.format("%02d",boxInfo.getBoardNum())+AppApplication.getInstance().getResources().getString(R.string.ex_rack)+
				String.format("%02d",boxInfo.getBoxNum())+"号";
	}
	
	public static String getBoxDesc(String boxId)
	{
		BoxInfo boxInfo = getBoxByCode(boxId);
		
		return getBoxDesc(boxInfo);
	}

}
