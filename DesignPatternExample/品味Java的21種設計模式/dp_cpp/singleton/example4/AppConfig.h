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
				namespace example4
				{

					///
					/// <summary> * ��ȡӦ�������ļ�������ʵ�� </summary>
					/// 
					class AppConfig
					{
					///	
					///	 <summary> * ����һ���������洢�����õ���ʵ����ֱ�������ﴴ����ʵ����ֻ�ᴴ��һ�� </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static AppConfig *instance = new AppConfig();
					///	
					///	 <summary> * ����һ��������Ϊ�ͻ����ṩAppConfig���ʵ�� </summary>
					///	 * <returns> һ��AppConfig��ʵ�� </returns>
					///	 
					public:
						static AppConfig *getInstance();

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
					///	 <summary> * ˽�л����췽�� </summary>
					///	 
					private:
						AppConfig();
					///	
					///	 <summary> * ��ȡ�����ļ����������ļ��е����ݶ��������õ������� </summary>
					///	 
						void readConfig();

					};

				}
			}
		}
	}
}