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
				namespace example1
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
					};

				}
			}
		}
	}
}