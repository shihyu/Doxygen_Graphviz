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
				namespace example2
				{

					///
					/// <summary> * ����ʾ�� </summary>
					/// 
					class Singleton
					{
					///	
					///	 <summary> * ����һ���������洢�����õ���ʵ�� </summary>
					///	 
					private:
						static Singleton uniqueInstance;
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