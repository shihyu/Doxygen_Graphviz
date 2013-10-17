#pragma once

#include "ExportHeaderModel.h"
#include "ExportDataModel.h"
#include "ExportFooterModel.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example3
				{


					///
					/// <summary> * �������ӿڣ����崴��һ������ļ���������ĸ��������Ĳ��� </summary>
					/// 
					class Builder
					{
					///	
					///	 <summary> * ��������ļ���Header���� </summary>
					///	 * <param name="ehm"> �ļ�ͷ������ </param>
					///	 
					public:
						virtual public void buildHeader(ExportHeaderModel*) = 0;
					///	
					///	 <summary> * ��������ļ���Body���� </summary>
					///	 * <param name="mapData"> Ҫ��������ݵ����� </param>
					///	 
						virtual public void buildBody(Map<std::string, Collection<ExportDataModel*>*>*) = 0;
					///	
					///	 <summary> * ��������ļ���Footer���� </summary>
					///	 * <param name="efm"> �ļ�β������ </param>
					///	 
						virtual public void buildFooter(ExportFooterModel*) = 0;
					};
				}
			}
		}
	}
}