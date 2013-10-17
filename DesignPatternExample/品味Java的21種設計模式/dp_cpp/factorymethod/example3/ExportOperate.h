#pragma once

#include "ExportFileApi.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace factorymethod
			{
				namespace example3
				{
					///
					/// <summary> * ʵ�ֵ������ݵ�ҵ���ܶ��� </summary>
					/// 
					class ExportOperate
					{
					///	
					///	 <summary> * �����ļ� </summary>
					///	 * <param name="data"> ��Ҫ��������� </param>
					///	 * <returns> �Ƿ�ɹ������ļ� </returns>
					///	 
					public:
						virtual bool export(std::string data);
					///	
					///	 <summary> * ���������������������ļ�����Ľӿڶ��� </summary>
					///	 * <returns> �������ļ�����Ľӿڶ��� </returns>
					///	 
					protected:
						virtual ExportFileApi *factoryMethod() = 0;
					};

				}
			}
		}
	}
}