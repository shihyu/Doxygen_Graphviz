#pragma once

#include <string>
#include <map>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace singleton
			{
				namespace example8
				{
					///
					/// <summary> * ʹ�û�����ģ��ʵ�ֵ��� </summary>
					/// 
					class Singleton
					{
					///	
					///	 <summary> * ����һ��ȱʡ��keyֵ��������ʶ�ڻ����еĴ�� </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static const std::string DEFAULT_KEY = "One";
					///	
					///	 <summary> * ����ʵ�������� </summary>
					///	 
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Map<std::string, Singleton> *map = std::map<std::string, Singleton>();
					///	
					///	 <summary> * ˽�л����췽�� </summary>
					///	 
						Singleton();
					public:
						static Singleton getInstance();
					};

				}
			}
		}
	}
}