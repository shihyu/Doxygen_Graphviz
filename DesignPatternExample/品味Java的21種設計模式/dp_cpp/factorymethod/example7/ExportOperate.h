#pragma once

#include "ExportFileApi.h"
#include "ExportTxtFile.h"
#include "ExportDB.h"
#include <string>

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
					/// <summary> * ʵ�ֵ������ݵ�ҵ���ܶ��� </summary>
					/// 
					class ExportOperate
					{
					///	
					///	 <summary> * �����ļ� </summary>
					///	 * <param name="type"> �û�ѡ��ĵ������� </param>
					///	 * <param name="data"> ��Ҫ��������� </param>
					///	 * <returns> �Ƿ�ɹ������ļ� </returns>
					///	 
					public:
						virtual bool export(int type, std::string data);
					///	
					///	 <summary> * ���������������������ļ�����Ľӿڶ��� </summary>
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