#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example4
				{
					///
					/// <summary> * ���㽱�������ӿ� </summary>
					/// 
					class Component
					{
					///	
					///	 <summary> * ����ĳ����ĳ��ʱ���ڵĽ�����Щ��������ʾ�в�����ʹ�ã�
					///	 * ������ʵ��ҵ��ʵ�����ǻ��õģ�Ϊ�˱�ʾ���Ǹ������ҵ�񷽷���
					///	 * �����Щ������������ </summary>
					///	 * <param name="user"> �����㽱�����Ա </param>
					///	 * <param name="begin"> ���㽱��Ŀ�ʼʱ�� </param>
					///	 * <param name="end"> ���㽱��Ľ���ʱ�� </param>
					///	 * <returns> ĳ����ĳ��ʱ���ڵĽ��� </returns>
					///	 
					public:
						virtual double calcPrize(std::string user, Date *begin, Date *end) = 0;
					};

				}
			}
		}
	}
}