#pragma once

#include "ExportOperate.h"
#include "ExportFileApi.h"
#include "ExportXml.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example7
				{
					///
					/// <summary> * ��չExportOperate���󣬼�����Ե���XML�ļ� </summary>
					/// 
					class ExportOperate2 : public ExportOperate
					{
					///	
					///	 <summary> * ���Ǹ���Ĺ��������������������ļ�����Ľӿڶ��� </summary>
					///	 * <param name="type"> �û�ѡ��ĵ������� </param>
					///	 * <returns> �������ļ�����Ľӿڶ��� </returns>
					///	 
					protected:
						virtual ExportFileApi *factoryMethod(int type);
					};

				}
			}
		}
	}
}