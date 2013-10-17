#pragma once

#include "Builder.h"
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
				namespace example3
				{


					///
					/// <summary> * ʵ�ֵ������ݵ�XML�ļ��ĵĹ��������� </summary>
					/// 
					class XmlBuilder : public Builder
					{
					///	
					///	 <summary> * ������¼�������ļ������ݣ��൱�ڲ�Ʒ </summary>
					///	 
					private:
						StringBuffer *buffer;
					public:
						virtual void buildBody(Map<std::string, Collection<ExportDataModel*>*> *mapData);

						virtual void buildFooter(ExportFooterModel *efm);

						virtual void buildHeader(ExportHeaderModel *ehm);
						virtual StringBuffer *getResult();


					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								buffer = new StringBuffer();

								initialized = true;
							}
						}

public:
	XmlBuilder()
	{
		InitializeInstanceFields();
	}
					};
				}
			}
		}
	}
}