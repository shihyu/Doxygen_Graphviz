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
				namespace example1
				{
					///
					/// <summary> * ���������ݿⱸ���ļ���ʽ�Ķ��� </summary>
					/// 
					class ExportDB : public ExportFileApi
					{
					public:
						virtual bool export(std::string data);
					};

				}
			}
		}
	}
}