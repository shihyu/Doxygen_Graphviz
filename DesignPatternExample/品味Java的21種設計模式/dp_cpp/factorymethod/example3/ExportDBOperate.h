#pragma once

#include "ExportOperate.h"
#include "ExportFileApi.h"
#include "ExportDB.h"

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
					/// <summary> * ����Ĵ�����ʵ�ֶ���ʵ�ִ������������ݿⱸ���ļ���ʽ�Ķ��� </summary>
					/// 
					class ExportDBOperate : public ExportOperate
					{
					protected:
						virtual ExportFileApi *factoryMethod();
					};

				}
			}
		}
	}
}