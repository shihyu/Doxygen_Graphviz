#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example1
				{

					///
					/// <summary> * ��ȡӦ�������ļ� </summary>
					/// 
					class AppConfig
					{
					///	
					///	 <summary> * ������������ļ��в���A��ֵ </summary>
					///	 
					private:
						std::string parameterA;
					///	
					///	 <summary> * ������������ļ��в���B��ֵ </summary>
					///	 
						std::string parameterB;

					public:
						virtual std::string getParameterA();
						virtual std::string getParameterB();
					///	
					///	 <summary> * ���췽�� </summary>
					///	 
						AppConfig();
					///	
					///	 <summary> * ��ȡ�����ļ����������ļ��е����ݶ��������õ������� </summary>
					///	 
					private:
						void readConfig();

					};

				}
			}
		}
	}
}