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
					/// <summary> * �������ı��ļ���ʽ�Ķ��� </summary>
					/// 
					class ExportTxtFile : public ExportFileApi
					{
					public:
						virtual bool export(std::string data);
					};

				}
			}
		}
	}
}