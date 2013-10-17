#pragma once

#include "Builder.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace builder
			{
				namespace example2
				{
					///
					/// <summary> * ָ���ߣ�ָ��ʹ�ù������Ľӿ���������Ʒ�Ķ��� </summary>
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
					///	 <summary> * ʾ�ⷽ����ָ���������������յĲ�Ʒ���� </summary>
					///	 
						virtual void construct();
					};
				}
			}
		}
	}
}