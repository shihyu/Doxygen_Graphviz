#pragma once

#include "ExportDataModel.h"
#include "ExportFooterModel.h"
#include "ExportHeaderModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example1
				{
					///
					/// <summary> * �������ݵ��ı��ļ��Ķ��� </summary>
					/// 
					class ExportToTxt
					{
					///	
					///	 <summary> * �������ݵ��ı��ļ� </summary>
					///	 * <param name="ehm"> �ļ�ͷ������ </param>
					///	 * <param name="mapData"> ���ݵ����� </param>
					///	 * <param name="efm"> �ļ�β������ </param>
					///	 
					public:
						virtual void export(ExportHeaderModel *ehm, Map<std::string, Collection<ExportDataModel*>*> *mapData, ExportFooterModel *efm);
					};

				}
			}
		}
	}
}