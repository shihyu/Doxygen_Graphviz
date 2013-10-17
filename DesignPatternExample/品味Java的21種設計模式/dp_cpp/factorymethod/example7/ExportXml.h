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
				namespace example7
				{
					///
					/// <summary> * ������xml�ļ��Ķ��� </summary>
					/// 
					class ExportXml : public ExportFileApi
					{
					public:
						virtual bool export(std::string data);
					};

				}
			}
		}
	}
}