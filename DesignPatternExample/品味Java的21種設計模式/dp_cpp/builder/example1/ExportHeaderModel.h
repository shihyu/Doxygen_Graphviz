#pragma once

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
					/// <summary> * ����������ļ�ͷ�����ݵĶ��� </summary>
					/// 
					class ExportHeaderModel
					{
					///	
					///	 <summary> * �ֹ�˾�����е��� </summary>
					///	 
					private:
						std::string depId;
					///	
					///	 <summary> * �������ݵ����� </summary>
					///	 
						std::string exportDate;
					public:
						virtual std::string getDepId();
						virtual void setDepId(std::string depId);
						virtual std::string getExportDate();
						virtual void setExportDate(std::string exportDate);
					};

				}
			}
		}
	}
}