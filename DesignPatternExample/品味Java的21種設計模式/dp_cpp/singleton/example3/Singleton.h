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
				namespace example3
				{
					///
					/// <summary> * ����ʽ����ʵ�ֵ�ʾ�� </summary>
					/// 
					class Singleton
					{
					///	
					///	 <summary> * ����һ���������洢�����õ���ʵ����ֱ�������ﴴ����ʵ����ֻ�ᴴ��һ�� </summary>
					///	 
					private:
//JAVA TO C++ CONVERTER TODO TASK: Native C++ only allows initialization of static const integral fields in their declarations:
						static Singleton uniqueInstance = new Singleton();
					///	
					///	 <summary> * ˽�л����췽���������ڲ����ƴ���ʵ������Ŀ </summary>
					///	 
						Singleton();
					///	
					///	 <summary> * ����һ��������Ϊ�ͻ����ṩ��ʵ�� </summary>
					///	 * <returns> һ��Singleton��ʵ�� </returns>
					///	 
					public:
						static Singleton getInstance();

					///	
					///	 <summary> * ʾ�ⷽ���������������Լ��Ĳ��� </summary>
					///	 
						virtual void singletonOperation();
					///	
					///	 <summary> * ʾ�����ԣ������������Լ������� </summary>
					///	 
					private:
						std::string singletonData;
					///	
					///	 <summary> * ʾ�ⷽ�������ⲿͨ����Щ�������������Ե�ֵ </summary>
					///	 * <returns> ���Ե�ֵ </returns>
					///	 
					public:
						virtual std::string getSingletonData();
					};

				}
			}
		}
	}
}