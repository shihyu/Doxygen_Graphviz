#pragma once

#include "ExportOperate.h"
#include "ExportFileApi.h"
#include "ExportTxtFile.h"

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
					/// <summary> * ����Ĵ�����ʵ�ֶ���ʵ�ִ����������ı��ļ���ʽ�Ķ��� </summary>
					/// 
					class ExportTxtFileOperate : public ExportOperate
					{

					protected:
						virtual ExportFileApi *factoryMethod();

					};

				}
			}
		}
	}
}