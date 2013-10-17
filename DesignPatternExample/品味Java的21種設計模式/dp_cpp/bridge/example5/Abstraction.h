#pragma once

#include "Implementor.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace bridge
			{
				namespace example5
				{

					///
					/// <summary> * ������󲿷ֵĽӿ� </summary>
					/// 
					class Abstraction
					{
					///	
					///	 <summary> * ����һ��ʵ�ֲ��ֵĶ��� </summary>
					///	 
					protected:
						Implementor *impl;
					///	
					///	 <summary> * ���췽��������ʵ�ֲ��ֵĶ���  </summary>
					///	 * <param name="impl"> ʵ�ֲ��ֵĶ��� </param>
					///	 
					public:
						Abstraction(Implementor *impl);
					///	
					///	 <summary> * ʾ��������ʵ��һ���Ĺ��ܣ�������Ҫת��ʵ�ֲ��ֵľ���ʵ�ַ��� </summary>
					///	 
						virtual void operation();
					};


				}
			}
		}
	}
}