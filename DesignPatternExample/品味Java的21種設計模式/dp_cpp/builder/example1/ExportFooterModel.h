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
					/// <summary> * ����������ļ�β�����ݵĶ��� </summary>
					/// 
					class ExportFooterModel
					{
					///	
					///	 <summary> * ����� </summary>
					///	 
					private:
						std::string exportUser;

					public:
						virtual std::string getExportUser();

						virtual void setExportUser(std::string exportUser);

					};

				}
			}
		}
	}
}