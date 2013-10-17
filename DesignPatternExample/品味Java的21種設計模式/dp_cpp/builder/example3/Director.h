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
					/// <summary> * ָ���ߣ�ָ��ʹ�ù������Ľӿ�������������ļ��Ķ��� </summary>
					/// 
					class Director
					{
					///	
					///	 <summary> * ���е�ǰ��Ҫʹ�õĹ��������� </summary>
					///	 
					private:
						Builder *builder;
					///	
					///	 <summary> * ���췽�������빹�������� </summary>
					///	 * <param name="builder"> ���������� </param>
					///	 
					public:
						Director(Builder *builder);
					///	
					///	 <summary> * ָ���������������յ�������ļ��Ķ��� </summary>
					///	 * <param name="ehm"> �ļ�ͷ������ </param>
					///	 * <param name="mapData"> ���ݵ����� </param>
					///	 * <param name="efm"> �ļ�β������ </param>
					///	 
						virtual void construct(ExportHeaderModel *ehm, Map<std::string, Collection<ExportDataModel*>*> *mapData, ExportFooterModel *efm);
					};
				}
			}
		}
	}
}